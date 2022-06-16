package di;

import java.util.*;


class DependencyTree<d extends Dependency> extends HashSet<Dependency> {

}

abstract class Dependency {

    private final Set<Dependency> Dependants;

    protected Dependency(Set<Dependency> dependants) {
        Dependants = dependants;
    }

    abstract boolean isInitialized();

     public Set<Dependency> getDependants(){
         return Dependants;
     }
}

class UninitializedDependency extends Dependency{

    UninitializedDependency(Set<Dependency> dependants) {
        super(dependants);
    }

    @Override
    boolean isInitialized() {
        return false;
    }
}

class InitializedDependency extends Dependency {

    private final Set<Class<?>> interfaces;

    InitializedDependency(Set<Dependency> dependants,Set<Class<?>> interfaces) {
        super(dependants);
        this.interfaces = interfaces;
    }

    Set<Class<?>> getInterface() {
        return interfaces;
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

}