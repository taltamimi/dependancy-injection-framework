package zenfer;

import company.*;
import di.Service;

@Service
public class AboZnefer implements CEO {
    private final Technology tech;
    private final Operation opt;
    private final SharedServices sharedServices;
    private final ProductDevelopment pd;

    public AboZnefer() {
        this.tech = new RareGuy(new ChillDeveloper(), new Majeed());
        this.opt = new TheHatter(new MichaelScott(), new Elon());
        this.sharedServices = new HumanResources();
        this.pd = new TheHatter(new MichaelScott(), new Elon());
    }


    @Override
    public void makeMoney() {
        String newProject = pd.greatestIdea();
        tech.creteProduct(newProject);
        opt.CreateMarketingCampaign(newProject);
    }
}
