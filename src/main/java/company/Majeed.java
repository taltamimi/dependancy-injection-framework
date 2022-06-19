package company;

import di.Service;

@Service
public class Majeed implements SRE {
    @Override
    public void createRelease(String productName) {
        System.out.printf("give me the hard-disk to release %s $n", productName);
    }
}
