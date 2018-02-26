package ind.xwm.imooc.concurrence.basicThread.No1_WaitAndNotify;

public class PhoneMain {
    public static void main(String[] args) {
        PhoneStorage<Phone> storage = new PhoneStorage<Phone>();
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new PhoneCreator(storage));
            thread.start();
        }

        for(int i=0;i<5;i++) {
            Thread thread = new Thread(new PhoneSalesman(storage));
            thread.start();
        }
    }
}
