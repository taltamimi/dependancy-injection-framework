package company;

import di.Service;

@Service
public class Chad implements CEO {
    private final Technology tech;
    private final Operation opt;
    private final SharedServices sharedServices;
    private final ProductDevelopment pd;

    public Chad(Technology tech, Operation opt, SharedServices sharedServices, ProductDevelopment pd) {
        this.tech = tech;
        this.opt = opt;
        this.sharedServices = sharedServices;
        this.pd = pd;
    }


    @Override
    public void makeMoney() {
        String newProject = pd.greatestIdea();
        tech.creteProduct(newProject);
        opt.CreateMarketingCampaign(newProject);
    }
}
