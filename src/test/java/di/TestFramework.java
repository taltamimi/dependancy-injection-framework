package di;

import company.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class TestFramework {


    @Test
    public void testDecoratedClasses() {
        assertThat(DiFramework.getDecoratedClasses()).hasSize(1);
    }

    @Test
    public void testInstance() {
        assertThat(DiFramework.createInstance(TheHatter.class).isPresent()).isTrue();
    }

    @Test
    public void testDependants() {
        assertThat(DiFramework.getDependants(TheHatter.class)).isEmpty();
    }

    @Test
    public void testInterfaces() {
        assertThat(DiFramework.getInterfaces(TheHatter.class)).contains(Operation.class);
        assertThat(DiFramework.getInterfaces(TheHatter.class)).hasSize(1);
    }


    @Test
    public void testCreateDependencies() {
        DependencyTree dependencyTree = new DependencyTree();
        Dependency dependant = new Dependency(Set.of(CEO.class), Set.of());
        dependencyTree.createDependencies(dependant, Set.of(Development.class, SharedServices.class));
        dependencyTree.createDependencies(dependant, Set.of(Development.class, SharedServices.class));
        assertThat(dependencyTree.getDependencyByClass(Development.class).isPresent()).isTrue();
        assertThat(dependencyTree.getDependencyByClass(SharedServices.class).isPresent()).isTrue();
        assertThat(dependencyTree.getDependencyByClass(SharedServices.class).get().isInitialized()).isFalse();
    }

    @Test
    public void testCreateProvider() {
        DependencyTree dependencyTree = new DependencyTree();
        dependencyTree.mergeDependenciesToInitializedProvider(TheHatter.class);
        Optional<Dependency> optionalProvider = dependencyTree.getDependencyByClass(TheHatter.class);
        assertThat(optionalProvider.isPresent()).isTrue();
        Dependency provider = optionalProvider.get();
        assertThat(provider.getProviders()).containsExactly(TheHatter.class, Operation.class);
        assertThat(provider.isInitialized()).isTrue();
    }
    @Test
    public void testMergerOfDependencies() {
        DependencyTree dependencyTree = new DependencyTree();
        dependencyTree.mergeDependenciesToInitializedProvider(ChillDeveloper.class);
        dependencyTree.mergeDependenciesToInitializedProvider(RareGuy.class);
        dependencyTree.mergeDependenciesToInitializedProvider(Majeed.class);


        Dependency rareGuy = dependencyTree.getDependencyByClass(RareGuy.class).orElseThrow();
        Dependency majeed = dependencyTree.getDependencyByClass(Majeed.class).orElseThrow();
        Dependency chillGuy = dependencyTree.getDependencyByClass(ChillDeveloper.class).orElseThrow();


        assertThat(chillGuy.getDependents()).containsExactly(rareGuy);
        assertThat(majeed.getDependents()).containsExactly(rareGuy);

    }
//
//    @Test
//    public void testDependencyTree() {
//        DependencyTree tree = new DependencyTree();
//        tree.add(CoolCOO.class);
//        assertThat(tree.isResolved()).isTrue();
//
//    }
}
