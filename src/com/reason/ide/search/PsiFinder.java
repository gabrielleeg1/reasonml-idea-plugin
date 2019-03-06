package com.reason.ide.search;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.reason.Log;
import com.reason.build.bs.Bucklescript;
import com.reason.build.bs.BucklescriptManager;
import com.reason.ide.files.FileBase;
import com.reason.lang.core.ORFileType;
import com.reason.lang.core.PsiFileHelper;
import com.reason.lang.core.psi.*;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.intellij.psi.search.GlobalSearchScope.allScope;

public final class PsiFinder implements ProjectComponent {

    private static final Log LOG = Log.create("finder");

    @NotNull
    private final Project m_project;

    public PsiFinder(@NotNull Project project) {
        m_project = project;
    }

    public static PsiFinder getInstance(@NotNull Project project) {
        return project.getComponent(PsiFinder.class);
    }

    @Nullable
    public PsiInnerModule findComponent(@NotNull String fqn, @NotNull GlobalSearchScope scope) {
        ModuleComponentIndex index = ModuleComponentIndex.getInstance();

        Collection<PsiInnerModule> modules = index.get(fqn, m_project, scope);
        if (modules.isEmpty()) {
            return null;
        }

        Bucklescript bucklescript = BucklescriptManager.getInstance(m_project);
        PsiInnerModule module = modules.iterator().next();
        return bucklescript.isDependency(module.getContainingFile().getVirtualFile()) ? module : null;
    }

    @NotNull
    public Collection<PsiInnerModule> findComponents(@NotNull GlobalSearchScope scope) {
        ModuleComponentIndex index = ModuleComponentIndex.getInstance();
        Bucklescript bucklescript = BucklescriptManager.getInstance(m_project);

        return index.getAllKeys(m_project).
                stream().
                map(key -> index.getUnique(key, m_project, scope)).
                filter(module -> module != null && bucklescript.isDependency(module.getContainingFile().getVirtualFile())).
                collect(Collectors.toList());
    }

    @NotNull
    public Collection<PsiModule> findModules(@NotNull String name, @NotNull ORFileType fileType, @NotNull GlobalSearchScope scope) {
        List<PsiModule> result = new ArrayList<>();
        LOG.debug("Find modules, name", name);

        VirtualFile file;

        FileModuleIndexService fileModuleIndex = FileModuleIndexService.getService();
        if (fileType == ORFileType.implementationOnly) {
            Collection<VirtualFile> implementations = fileModuleIndex.getImplementationFilesWithName(name, scope);
            file = implementations.isEmpty() ? null : implementations.iterator().next();
        } else if (fileType == ORFileType.interfaceOnly) {
            Collection<VirtualFile> interfaces = fileModuleIndex.getInterfaceFilesWithName(name, scope);
            file = interfaces.isEmpty() ? null : interfaces.iterator().next();
        } else {
            file = fileModuleIndex.getFileWithName(name, scope);
        }

        if (file == null) {
            LOG.debug("  No file module found");
        } else {
            LOG.debug("  file module found", file);
            result.add((PsiModule) PsiManager.getInstance(m_project).findFile(file));
        }

        Collection<PsiInnerModule> modules = ModuleIndex.getInstance().get(name, m_project, scope);
        if (modules.isEmpty()) {
            LOG.debug("  No inner modules found");
        } else {
            LOG.debug("  inner modules found", modules.size());
            for (PsiInnerModule module : modules) {
                String filename = ((FileBase) module.getContainingFile()).asModuleName();

                if (fileType == ORFileType.implementationOnly) {
                    Collection<VirtualFile> implementations = fileModuleIndex.getImplementationFilesWithName(filename, scope);
                    file = implementations.isEmpty() ? null : implementations.iterator().next();
                } else if (fileType == ORFileType.interfaceOnly) {
                    Collection<VirtualFile> interfaces = fileModuleIndex.getInterfaceFilesWithName(filename, scope);
                    file = interfaces.isEmpty() ? null : interfaces.iterator().next();
                } else {
                    file = fileModuleIndex.getFileWithName(filename, scope);
                }

                if (file == null) {
                    LOG.debug("    abandon", module);
                } else {
                    LOG.debug("       keep", module);
                    result.add(module);
                }
            }
        }

        return result;
    }

    @Nullable
    public PsiModule findModule(@NotNull String name, @NotNull ORFileType fileType, GlobalSearchScope scope) {
        Collection<PsiModule> modules = findModules(name, fileType, scope);
        if (!modules.isEmpty()) {
            return modules.iterator().next();
        }

        return null;
    }

    @NotNull
    public Collection<PsiLet> findLets(@NotNull String name, @NotNull ORFileType fileType) {
        return findLowerSymbols("lets", name, fileType, IndexKeys.LETS, PsiLet.class, allScope(m_project));
    }

    @NotNull
    public Collection<PsiVal> findVals(@NotNull String name, @NotNull ORFileType fileType) {
        return findLowerSymbols("vals", name, fileType, IndexKeys.VALS, PsiVal.class, allScope(m_project));
    }

    @NotNull
    public Collection<PsiType> findTypes(@NotNull String name, @NotNull ORFileType fileType) {
        return findLowerSymbols("types", name, fileType, IndexKeys.TYPES, PsiType.class, allScope(m_project));
    }

    @NotNull
    public Collection<PsiExternal> findExternals(@NotNull String name, @NotNull ORFileType fileType) {
        return findLowerSymbols("externals", name, fileType, IndexKeys.EXTERNALS, PsiExternal.class, allScope(m_project));
    }

    private <T extends PsiQualifiedNamedElement> Collection<T> findLowerSymbols(@NotNull String debugName, @NotNull String name, @NotNull ORFileType fileType, @NotNull StubIndexKey<String, T> indexKey, @NotNull Class<T> clazz, @NotNull GlobalSearchScope scope) {
        Map<String/*qn*/, T> inConfig = new THashMap<>();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Find " + debugName + " name", name);
        }

        FileModuleIndexService fileModuleIndex = FileModuleIndexService.getService();

        Collection<T> items = StubIndex.getElements(indexKey, name, m_project, scope, clazz);
        if (items.isEmpty()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("  No " + debugName + " found");
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("  " + debugName + " found", items.size(), items);
            }
            for (T item : items) {
                String filename = ((FileBase) item.getContainingFile()).asModuleName();
                VirtualFile file;

                if (fileType == ORFileType.implementationOnly) {
                    Collection<VirtualFile> implementations = fileModuleIndex.getImplementationFilesWithName(filename, scope);
                    file = implementations.isEmpty() ? null : implementations.iterator().next();
                } else if (fileType == ORFileType.interfaceOnly) {
                    Collection<VirtualFile> interfaces = fileModuleIndex.getInterfaceFilesWithName(filename, scope);
                    file = interfaces.isEmpty() ? null : interfaces.iterator().next();
                } else {
                    file = fileModuleIndex.getFileWithName(filename, scope);
                }

                if (file != null) {
                    LOG.debug("    keep (in config)", item);
                    inConfig.put(item.getQualifiedName(), item);
                }
            }
        }

        return inConfig.values();
    }

    @Nullable
    public PsiQualifiedNamedElement findModuleAlias(@Nullable String moduleQname) {
        if (moduleQname == null) {
            return null;
        }

        GlobalSearchScope scope = allScope(m_project);
        Collection<PsiInnerModule> modules = ModuleFqnIndex.getInstance().get(moduleQname.hashCode(), m_project, scope);

        if (!modules.isEmpty()) {
            PsiInnerModule moduleReference = modules.iterator().next();
            String alias = moduleReference.getAlias();

            if (alias != null) {
                VirtualFile vFile = FileModuleIndexService.getService().getFileWithName(alias, scope);
                if (vFile != null) {
                    return (FileBase) PsiManager.getInstance(m_project).findFile(vFile);
                }

                modules = ModuleFqnIndex.getInstance().get(alias.hashCode(), m_project, scope);
                if (!modules.isEmpty()) {
                    PsiInnerModule next = modules.iterator().next();
                    if (next != null) {
                        PsiQualifiedNamedElement nextModuleAlias = findModuleAlias(next.getQualifiedName());
                        return nextModuleAlias == null ? next : nextModuleAlias;
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    public PsiQualifiedNamedElement findModuleFromQn(@Nullable String moduleQName) {
        if (moduleQName == null) {
            return null;
        }

        // extract first token of path
        String[] names = moduleQName.split("\\.");

        VirtualFile vFile = FileModuleIndexService.getService().getFileWithName(names[0], allScope(m_project));
        if (vFile != null) {
            FileBase fileModule = (FileBase) PsiManager.getInstance(m_project).findFile(vFile);
            if (1 < names.length) {
                PsiQualifiedNamedElement currentModule = fileModule;
                for (int i = 1; i < names.length; i++) {
                    String innerModuleName = names[i];
                    currentModule = currentModule instanceof FileBase ? PsiFileHelper.getModuleExpression((PsiFile) currentModule, innerModuleName) : ((PsiInnerModule) currentModule).getModule(innerModuleName);
                    if (currentModule == null) {
                        return null;
                    }
                }
                return currentModule;
            }

            return fileModule;
        }

        return null;
    }
}