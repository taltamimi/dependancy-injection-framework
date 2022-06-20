package company;

import di.DiFramework;
import zenfer.AboZnefer;

import java.util.Map;

public class MimicCompany {
    public static void main(String[] args) {
        DiFramework.initialize();


        System.out.println("================Start============================");


        CEO ceo = (CEO) DiFramework.initialize().entrySet().stream().filter(kv -> kv.getKey().contains(CEO.class))
                .map(Map.Entry::getValue).findFirst().get();
        ceo.makeMoney();

        System.out.println("================ DI ============================");


        new AboZnefer().makeMoney();


        System.out.println("================ End ============================");
    }
}
