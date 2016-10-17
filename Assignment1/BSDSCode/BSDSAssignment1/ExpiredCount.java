package BSDSAssignment1;

/**
 * Created by soumya on 10/16/16.
 */
public class ExpiredCount {
    private int expiredMsgCount = 0;

    public synchronized void incrementCount() {
        expiredMsgCount++;
    }

    public synchronized void decrementCount() {
        expiredMsgCount--;
    }

    public synchronized int getCount() {
        return expiredMsgCount;
    }
}
