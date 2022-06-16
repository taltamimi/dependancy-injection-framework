import company.AwesomeGuy;
import company.CEO;
import di.DiFramework;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class TestFramework {


    @Test
    public void testDecoratedClasses() {
        assertThat(new DiFramework().getDecoratedClasses()).hasSize(1);
    }

    @Test
    public void testInstance() {
        assertThat(new DiFramework().createInstance(AwesomeGuy.class).isPresent()).isTrue();
    }

    @Test
    public void testInterfaces() {
        assertThat(new DiFramework().getInterfaces(AwesomeGuy.class)).contains(CEO.class);
        assertThat(new DiFramework().getInterfaces(AwesomeGuy.class)).hasSize(2);
    }

//    @Test
//    public void getService() {
//        assertThat(new DiFramework().init().get(CEO.class)).isNotNull();
//    }
}
