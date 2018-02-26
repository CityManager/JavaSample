package ind.xwm.imooc.concurrence.basicThread.No3_awaitAndSignal;

import ind.xwm.imooc.concurrence.basicThread.No1_WaitAndNotify.Phone;
import ind.xwm.imooc.concurrence.basicThread.No1_WaitAndNotify.PhoneCreator;
import ind.xwm.imooc.concurrence.basicThread.No1_WaitAndNotify.PhoneSalesman;
import ind.xwm.imooc.concurrence.basicThread.Storage;

public class AwaitSignalMain {
    public static void main(String[] args) {
        Storage<Phone> storage = new ReentrantPhoneStorage<>();
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new PhoneCreator(storage));
            thread.start();
        }

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new PhoneSalesman(storage));
            thread.start();
        }
    }
}
