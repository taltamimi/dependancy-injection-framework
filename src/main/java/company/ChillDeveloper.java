package company;

import di.Service;

@Service
public class ChillDeveloper implements Development {
    @Override
    public void createRelease(String productName) {
        System.out.printf("Developing product %s while chilling%n", productName);
    }
}
