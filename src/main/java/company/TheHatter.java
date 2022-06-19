package company;

import di.Service;

@Service
public class TheHatter implements Operation, ProductDevelopment{

    private final Sales sales;
    private final Marking marking;

    public TheHatter(Sales sales, Marking marking) {
        this.sales = sales;
        this.marking = marking;
    }


    @Override
    public void CreateMarketingCampaign(String projectName) {
        System.out.println("Successful campaign");
    }

    @Override
    public String greatestIdea() {
        return "great idea";
    }
}
