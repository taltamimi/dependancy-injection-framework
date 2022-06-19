package company;

import di.Service;

@Service
public class RareGuy implements Technology{

    private final Development development;
    private final SRE sre;


    public RareGuy(Development development, SRE sre){
        this.development = development;
        this.sre = sre;
    }

    @Override
    public void creteProduct(String productName) {
        development.createRelease(productName);
        sre.createRelease(productName);

    }
}
