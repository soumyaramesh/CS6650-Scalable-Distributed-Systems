/**
 * Created by Ian Gortan on 9/20/2016.
 */
package BSDSAssignment1;
// Contains content for message publication

import java.util.HashSet;

public class BSDSContent {
    final static int len = 140;
    private String title;
    private String message;
    private int timeToLIve;



    private long currentTimeMillis;


    private int deliveredCount;

    public BSDSContent(String title, String message, int timeToLive, int deliveredCount, long currentTimeMillis) {
        this.setTitle(title);
        this.setMessage(message);
        this.setTimeToLIve(timeToLive);
        this.setDeliveredCount(deliveredCount);
        this.setCurrentTimeMillis(currentTimeMillis);
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTimeToLIve() {
        return timeToLIve;
    }

    public void setTimeToLIve(int timeToLIve) {
        this.timeToLIve = timeToLIve;
    }

    public int getDeliveredCount() {
        return deliveredCount;
    }

    public void setDeliveredCount(int deliveredCount) {
        this.deliveredCount = deliveredCount;
    }

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public synchronized void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

}
