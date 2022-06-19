package company;

import di.Service;

@Service
public class HumanResources implements SharedServices {
    @Override
    public void hirePerson(String personName) {
        System.out.printf("hire %s after 6 months %n", personName);
    }
}
