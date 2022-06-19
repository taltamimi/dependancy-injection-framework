package di;

import company.*;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

public class TestFramework {


    @Test
    public void testDecoratedClasses() {
        assertThat(DiFramework.getDecoratedClasses("company")).hasSize(8);
    }


    @Test
    public void testDependants() {
        assertThat(DiFramework.getDependants(ChillDeveloper.class)).isEmpty();
    }

    @Test
    public void testInterfaces() {
        assertThat(DiFramework.getInterfaces(TheHatter.class)).containsExactly(Operation.class, ProductDevelopment.class);
        assertThat(DiFramework.getInterfaces(TheHatter.class)).hasSize(2);
    }


    @Test
    public void testCreateDependencies() {
        DependencyTree dependencyTree = new DependencyTree();
        ResolvedDependency dependant = new ResolvedDependency(Set.of(CEO.class), Set.of());
        dependencyTree.createDependencies(dependant, Set.of(Development.class, SharedServices.class));
        dependencyTree.createDependencies(dependant, Set.of(Development.class, SharedServices.class));
        assertThat(dependencyTree.getDependencyByClass(Development.class).isPresent()).isTrue();
        assertThat(dependencyTree.getDependencyByClass(SharedServices.class).isPresent()).isTrue();
        assertThat(dependencyTree.getDependencyByClass(SharedServices.class).get().isInitialized()).isFalse();
    }


    @Test
    public void testCreateProvider() {
        DependencyTree dependencyTree = new DependencyTree();
        DependencyTree.DependencyMerger mergedHatter = dependencyTree.mergeDependenciesToInitializedProvider(TheHatter.class);
//        Optional<Dependency> optionalProvider = dependencyTree.getDependencyByClass(TheHatter.class);
        assertThat(mergedHatter.ResolvedDependency().isInitialized()).isTrue();
        assertThat(mergedHatter.ResolvedDependency().dependents()).isEmpty();
        assertThat(mergedHatter.ResolvedDependency().providers()).containsExactly(TheHatter.class, Operation.class, ProductDevelopment.class);
        assertThat(mergedHatter.duplicatedDependencies()).isEmpty();

    }

    @Test
    public void testSimpleResolve() {
        DependencyTree tree = new DependencyTree();
        tree.add(Majeed.class);
        assertThat(tree.isResolved()).isTrue();
        tree.add(Chad.class);
        assertThat(tree.isResolved()).isFalse();
    }

    @Test
    public void testDependencyTree() {
        DependencyTree tree = new DependencyTree();
        Set<Class<?>> classes = Set.of(Chad.class, ChillDeveloper.class, Elon.class, HumanResources.class, Majeed.class, MichaelScott.class, RareGuy.class, TheHatter.class);
        classes.forEach(tree::add);
        assertThat(tree.isResolved()).isTrue();

        assertThat(tree
                .getDependencyByClass(TheHatter.class)
                .orElseThrow()
                .dependants()
                .stream()
                .flatMap(d -> d.providers().stream())
                .collect(Collectors.toSet())
        ).containsExactly(Chad.class, CEO.class);
    }

    @Test
    public void testGetDependency() {
        assertThat(DiFramework.getDependencies(TheHatter.class).requiredArgs()).asList().containsExactly(Marking.class, Sales.class);
    }

    @Test
    public void testInstance() {
        assertThat(DiFramework.createInstance(DiFramework.getDependencies(TheHatter.class), new MichaelScott(), new Elon())).isInstanceOf(TheHatter.class);
    }

    @Test
    public void testDiInitialization() {
        DiFramework.initialize();
    }

//    @Test
//    public void testGetRoot(){
//        DependencyTree tree = new DependencyTree();
//        Set<Class<?>> classes = Set.of(Chad.class, ChillDeveloper.class, Elon.class, HumanResources.class, Majeed.class, MichaelScott.class, RareGuy.class, TheHatter.class);
//        classes.forEach(tree::add);
//        tree.getRoot();
//    }
}
