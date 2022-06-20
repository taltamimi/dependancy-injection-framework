package di;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.function.Predicate.*;


interface Dependency {
    boolean isResolved();

    Set<Class<?>> providers();
    Dependency addDependant(Dependency dependent);

    Set<Dependency>  dependants();

}

record ResolvedDependency( Set<Class<?>> interfaces, Set<Dependency> dependents) implements Dependency {


    public Class<?> getImplementation(){
        return interfaces.stream().filter(not(Class::isInterface)).findFirst().orElseThrow();
    }
    public boolean isResolved() {
        return true;
    }


    public Dependency addDependant(Dependency dependent) {
        dependents.add(dependent);
        return this;
    }


    @Override
    public Set<Class<?>> providers() {
        return interfaces;
    }


    @Override
    public Set<Dependency> dependants() {
        return dependents;
    }
}

record UnresolvedDependency(Class<?> provider, Set<Dependency> dependents) implements Dependency {
    public UnresolvedDependency(Class<?> d, Dependency dependant) {
        this(d, new HashSet<>(List.of(dependant)));
    }


    public boolean isResolved() {
        return false;
    }

    @Override
    public Set<Class<?>> providers() {
        return Set.of(provider);
    }
    public Dependency addDependant(Dependency dependent) {
        dependents.add(dependent);
        return this;
    }

    @Override
    public Set<Dependency> dependants() {
        return dependents;
    }
}
