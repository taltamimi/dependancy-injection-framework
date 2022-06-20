package di;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;

import static java.util.stream.Collectors.*;

public class DiFramework {



    public static DependencyTree dependencyTree = new DependencyTree();

    public static HashMap<Set<Class<?>>, Object> initialize() {
        getDecoratedClasses("company")
                .forEach(aClass -> dependencyTree.add(aClass));
        if (!dependencyTree.isResolved())
            throw new RuntimeException("Unresolved Dependencies");

        HashMap<Set<Class<?>>, Object> constructedDependencies = new HashMap<>();

        Set<ResolvedDependency> dependencies =
                dependencyTree.dependencies.stream().map(d -> (ResolvedDependency) d).collect(toSet());
        constructTreeElements(constructedDependencies, dependencies);
        
        return constructedDependencies;
    }

    private static void constructTreeElements(HashMap<Set<Class<?>>, Object> constructedDependencies, Set<ResolvedDependency> dependencies) {
        while (dependencies.size() != 0) {
            for (ResolvedDependency dependency : dependencies) {
                DependencyConstructor<?> constructor = getDependencies(dependency.getImplementation());
                if (DependenciesInitialized(constructedDependencies, constructor)) {
                    Object[] objects = Arrays.stream(constructor.requiredArgs)
                            .map(
                                    arg -> constructedDependencies
                                            .entrySet()
                                            .stream()
                                            .filter(kv -> kv.getKey().contains(arg))
                                            .map(Map.Entry::getValue)
                                            .findFirst()
                                            .get()
                            ).toArray();

                    Object instance = createInstance(constructor, objects);
                    constructedDependencies.put(dependency.interfaces(), instance);
                    dependencies.remove(dependency);
                    break;
                }
            }
        }
    }

    private static boolean DependenciesInitialized(HashMap<Set<Class<?>>, Object> constructedDependencies, DependencyConstructor<?> constructor) {
        return Arrays
                .stream(constructor.requiredArgs())
                .allMatch(arg ->
                        constructedDependencies
                                .keySet()
                                .stream()
                                .anyMatch(c -> c.contains(arg)));
    }


//    public static Map<Class<?>,Object> crawlTree(){
//
//    }

    public static Set<Class<?>> getDecoratedClasses(String pkg) {
        return new Reflections(pkg).getTypesAnnotatedWith(Service.class);
    }


    record DependencyConstructor<t>(Constructor<t> constructor, Class<?>[] requiredArgs) {
    }

    public static <t> DependencyConstructor<t> getDependencies(Class<t> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 0)
                throw new ExceptionInInitializerError("No public constructor for class %s".formatted(clazz.getName()));
            else {
                Constructor<t> constructor = (Constructor<t>) constructors[0];
                return new DependencyConstructor<>(constructor, constructor.getParameterTypes());

            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static <T> T createInstance(DependencyConstructor<T> depConst, Object... instances) {
        try {
            return depConst.constructor.newInstance(instances);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<Class<?>> getInterfaces(Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces()).collect(toSet());

    }

    public static Set<Class<?>> getDependants(Class<?> clazz) {
        return Set.of(Arrays.stream(clazz.getConstructors()).findFirst().orElseThrow(() -> new IllegalStateException(String.format("Class %s has no public constructor", clazz.getName()))).getParameterTypes());
    }
}
