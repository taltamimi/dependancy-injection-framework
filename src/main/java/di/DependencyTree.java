package di;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.*;


public class DependencyTree  {

    Set<Dependency> dependencies = new HashSet<>();


    public void add(Class<?> clazz) {

        Set<Class<?>> dependants = DiFramework.getDependants(clazz);
        DependencyMerger dependencyMerger = mergeDependenciesToInitializedProvider(clazz);
        dependencies.add(dependencyMerger.ResolvedDependency());
        dependencyMerger.duplicatedDependencies().forEach(dependencies::remove);
        createDependencies(dependencyMerger.ResolvedDependency(), dependants);
    }


    void createDependencies(ResolvedDependency dependant, Set<Class<?>> dependencies) {
        dependencies.forEach(d -> {
            Optional<Dependency> dependency = getDependencyByClass(d);
            if (dependency.isPresent())
                dependency.get().addDependant(dependant);
            else
                this.dependencies.add(new UnresolvedDependency(d, dependant));
        });
    }

//    public Set<Dependency> getRoot() {
//        dependencies.stream().filter(dependency ->
//                        dependency.getImplementation
//                )
//
//        return null;
//    }

    record DependencyMerger(ResolvedDependency ResolvedDependency, Set<UnresolvedDependency> duplicatedDependencies) {
    }

    DependencyMerger mergeDependenciesToInitializedProvider(Class<?> impl) {

        Set<Class<?>> interfaces = Arrays.stream(impl.getInterfaces()).collect(Collectors.toSet());
        interfaces.add(impl);
        Set<UnresolvedDependency> uninitializedDependencies = getUninitializedDependencies(interfaces);
        Set<Dependency> dependants =
                uninitializedDependencies
                        .stream()
                        .flatMap(dep -> dep.dependents().stream())
                        .collect(Collectors.toSet());

        return new DependencyMerger(new ResolvedDependency(interfaces, dependants), uninitializedDependencies);


    }

    private Set<UnresolvedDependency> getUninitializedDependencies(Set<Class<?>> providers) {
        return dependencies
                .stream()
                .filter(not(Dependency::isResolved))
                .map(UnresolvedDependency.class::cast)
                .filter(dependency ->
                        providers.stream().anyMatch(aClass -> aClass.equals(dependency.provider()))
                ).collect(Collectors.toSet());
    }




    public boolean isResolved() {
        return dependencies.stream().allMatch((Dependency::isResolved));
    }


    public Optional<Dependency> getDependencyByClass(Class<?> clazz) {
        return dependencies.stream().filter(dep -> dep.providers().contains(clazz)).findFirst();
    }
}


