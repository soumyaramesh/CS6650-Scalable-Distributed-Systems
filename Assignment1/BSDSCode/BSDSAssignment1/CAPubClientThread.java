package BSDSAssignment1;

import java.rmi.RemoteException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by soumya on 10/15/16.
 */
public class CAPubClientThread implements Runnable {

    private BSDSPublishInterface CAServerStub;
    private String pubId;
    private String title;
    private String message;
    private int ttl;
    private int maxMessages;
    private CyclicBarrier barrier;

    public CAPubClientThread(BSDSPublishInterface CAServerStub, String id, String title, String message, int ttl, int maxMessages,
                             CyclicBarrier barrier) {
        this.CAServerStub = CAServerStub;
        this.pubId = id;
        this.title = title;
        this.message = message;
        this.ttl = ttl;
        this.maxMessages = maxMessages;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            for(int i=1;i<=maxMessages;i++) {
                CAServerStub.publishContent(pubId, title, message + i, ttl);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
