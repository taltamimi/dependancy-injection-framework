package company;

import di.Service;

@Service
public class Majeed implements SRE {

    Environment deploymentEnvironment = new NICWAN();

    @Override
    public void createRelease(String productName) {
        deploymentEnvironment.OpenPc();
        deploymentEnvironment.plugHardDisk();
        deploy(productName);
        deploymentEnvironment.UnplugHardDisk();
    }

    void deploy(String productName) {
        System.out.printf("deploying %S %n", productName);
    }


    public interface Environment {
        void OpenPc();

        void plugHardDisk();

        void UnplugHardDisk();
    }

    public class NICWAN implements Environment {

        @Override
        public void OpenPc() {
            System.out.println("Opening Nic PC");
        }

        @Override
        public void plugHardDisk() {
            System.out.println("plugging Nic hard disk");

        }

        @Override
        public void UnplugHardDisk() {
            System.out.println("unplugging Nic hard disk");

        }
    }
}
