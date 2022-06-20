package company;

import di.Service;

import java.util.Random;

@Service
public class ChillDeveloper implements Development {


    Random random = new Random();

    @Override
    public void createRelease(String productName) {
        int result = random.nextInt(2);
        if (result == 1)
            develop(new DevelopFast(), productName);
        else develop(new DevelopWhileChilling(), productName);

    }


    private void develop(
            DevelopmentMode mode,
            String productName
    ) {
        mode.Develop(productName);
    }

    private interface DevelopmentMode {
        void Develop(String productName);
    }

    public static class DevelopFast implements DevelopmentMode {
        @Override
        public void Develop(String productName) {
            System.out.printf("Developing product %s super fast %n", productName);
        }
    }

    public static class DevelopWhileChilling implements DevelopmentMode {
        @Override
        public void Develop(String productName) {
            System.out.printf("Developing product %s while chilling %n", productName);
        }
    }


}
