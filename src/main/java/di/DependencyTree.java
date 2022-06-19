package di;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.*;


public class DependencyTree {

    Set<Dependency> dependencies = new HashSet<>();

    public void mergeDependenciesToInitializedProvider(Class<?> provider) {
        Set<Class<?>> providers = Arrays.stream(provider.getInterfaces()).collect(Collectors.toSet());
        providers.add(provider);
        List<UninitializedDependency> uninitializedDependencies =
                dependencies
                        .stream()
                        .filter(not(Dependency::isInitialized))
                        .map(UninitializedDependency.class::cast)
                        .filter(dependency ->
                                providers.stream().anyMatch(aClass -> aClass.equals(dependency.getProvider()))
                        ).toList();

        uninitializedDependencies.forEach(dependencies::remove);

        Set<Dependency> dependants =
                uninitializedDependencies
                        .stream()
                        .flatMap(dep -> dep.getDependents().stream())
                        .collect(Collectors.toSet());

        dependencies.add(new Dependency(providers, dependants));
    }

    void createDependencies(Dependency dependant, Set<Class<?>> dependencies) {
        dependencies.forEach(d -> {
            Optional<Dependency> dependency = getDependencyByClass(d);
            if (dependency.isPresent())
                dependency.get().addDependant(dependant);
            else
                this.dependencies.add(new UninitializedDependency(d, dependant));
        });

    }

    public void add(Class<?> clazz) {

        Set<Class<?>> dependants = DiFramework.getDependants(clazz);
        Set<Class<?>> interfaces = DiFramework.getInterfaces(clazz);

    }
//    private Dependency createDependant(Class<?> dependent, Class<Dependency> dependency){
//        Optional<Dependency> optionalDependant = dependencies.stream().filter(dep -> dep.getProviders().contains(dependency)).findFirst();
//        if(optionalDependant.isPresent())
//            return optionalDependant.get().addDependant();
//        else
//    }


    public boolean isResolved() {
        return dependencies.stream().anyMatch(not(Dependency::isInitialized));
    }


    public Optional<Dependency> getDependencyByClass(Class<?> clazz) {
        return dependencies.stream().filter(dep -> dep.getProviders().contains(clazz)).findFirst();
    }
}

class Dependency {
    private final Set<Dependency> dependents;
    private final Set<Class<?>> interfaces;

    public Dependency(Set<Class<?>> interfaces, Set<Dependency> dependants) {
        this.dependents = dependants;
        this.interfaces = interfaces;
    }

    Set<Class<?>> getProviders() {
        return interfaces;
    }

    public Set<Dependency> getDependents() {
        return dependents;
    }

    boolean isInitialized() {
        return true;
    }

    public Dependency addDependant(Dependency dependent) {
        dependents.add(dependent);
        return this;
    }
}

class UninitializedDependency extends Dependency {
    //todo hashcode and equals
    private final Class<?> provider;


    Class<?> getProvider() {
        return provider;
    }

    Set<Class<?>> getProviders() {
        return Set.of(provider);
    }

    UninitializedDependency(Class<?> provider, Dependency dependant) {
        super(new HashSet<>(Set.of(provider)), new HashSet<>(Set.of(dependant)));
        this.provider = provider;
    }

    @Override
    boolean isInitialized() {
        return false;
    }
}
