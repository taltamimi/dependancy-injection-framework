package company;

import di.Service;

@Service
public class Chad implements CEO {
    private final Technology tech;
    private final Operation opt;
    private final SharedServices sharedServices;

    Chad(Technology tech, Operation opt, SharedServices sharedServices) {

        this.tech = tech;
        this.opt = opt;
        this.sharedServices = sharedServices;
    }

}
