package di;

import org.reflections.Reflections;

import java.util.*;

import static java.util.stream.Collectors.*;

public class DiFramework {

    private final Map<Class<?>, Object> context = new HashMap<>();


//    public TreeSet<Class<?>> getDependencyTree(){
//
//    }


    public Set<Class<?>> getDecoratedClasses() {
        return new Reflections("company").getTypesAnnotatedWith(Service.class);
    }


    public <T> Optional<T> createInstance(Class<T> clazz) {
        try {
            T instance = clazz.getConstructor(new Class[0]).newInstance();
            return Optional.of(instance);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Set<Class<?>> getInterfaces(Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces()).collect(toSet());

    }
}
