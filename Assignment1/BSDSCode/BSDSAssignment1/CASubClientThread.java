package BSDSAssignment1;

import java.rmi.RemoteException;

/**
 * Created by soumya on 10/14/16.
 */
public class CASubClientThread implements Runnable {
    private BSDSSubscribeInterface CAServerStub;
    private String subId;
    private int count;
    public CASubClientThread(BSDSSubscribeInterface CAServerStub, String subId, int count) {
        this.CAServerStub = CAServerStub;
        this.subId = subId;
        this.count = count;
    }

    @Override
    public void run() {
        String msg;
        int n =1;
        try {
            while(n<10) {
                while ((msg = CAServerStub.getLatestContent(subId)) == null) {
                    try {
                        Thread.sleep(n * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    n = n * 2;
                }
                System.out.println("Retrieved message = " + msg);
                count ++;
                n = 1;
            }
            System.out.println("Timeout reached");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
