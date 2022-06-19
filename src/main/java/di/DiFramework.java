package di;

import company.Elon;
import company.MichaelScott;
import company.TheHatter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.*;

import static java.util.stream.Collectors.*;

public class DiFramework {


    public static DependencyTree dependencyTree= new DependencyTree();
    public static void initialize() {
        getDecoratedClasses("company")
                .forEach(aClass -> dependencyTree.add(aClass));
        if(!dependencyTree.isResolved())
            throw new RuntimeException("Unresolved Dependencies");
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
