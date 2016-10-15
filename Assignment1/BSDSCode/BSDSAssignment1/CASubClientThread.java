package BSDSAssignment1;

import java.rmi.RemoteException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by soumya on 10/14/16.
 */
public class CASubClientThread implements Runnable {
    private BSDSSubscribeInterface CAServerStub;
    private String subId;
    private int count;
    private CyclicBarrier barrier;
    public CASubClientThread(BSDSSubscribeInterface CAServerStub, String subId, int count, CyclicBarrier barrier) {
        this.CAServerStub = CAServerStub;
        this.subId = subId;
        this.count = count;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        String msg;
        int n =1;
        try {
            while(n<10) {
                if((msg = CAServerStub.getLatestContent(subId)) != null) {
                    count ++;
                    System.out.println("Received message = " + msg);
                    System.out.println("Total messages received so far by subscriber Thread " +Thread.currentThread() + " = " + count);
                    n = 1;
                }
                else {
                    System.out.println("sleep for " + n*100 + " ms");
                    try {
                        Thread.sleep(n * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    n = n*2;
                }
            }
            System.out.println("Timeout reached");
            System.out.println("Count at the end of the thread = " + count);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            barrier.await();
            System.out.println("Count at end of thread " + Thread.currentThread() + " = " + count);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
