package BSDSAssignment1;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;

/**
 * Created by soumya on 10/16/16.
 */
public class DeleteExpiredMessagesThread implements Runnable {

    private ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, BSDSContent>> topicQueues;
    private int expiredCount;
    private CyclicBarrier barrier;

    public DeleteExpiredMessagesThread(ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, BSDSContent>> topicQueues) {
        this.topicQueues = topicQueues;
        this.expiredCount = 0;
        System.out.println("Started monitoring queues for expired messages");

    }

    @Override
    public void run() {
        //ScheduledExecutorService execService = Executors.newScheduledThreadPool(topicQueues.size());
        if (topicQueues.keySet().size() > 1) {
            System.out.println("In delete thread!!");
            ExecutorService executor = Executors.newFixedThreadPool(topicQueues.size());
            for (String topic : topicQueues.keySet()) {
                Map<Integer, BSDSContent> topicQueue = Collections.synchronizedMap(topicQueues.get(topic));
                if(topicQueue.size()<1) {
                    System.out.println("No more messages in this queue");
                    System.out.println("Total expired count = " + expiredCount);
                }
                else {
                    executor.execute(() -> {
                        for (Map.Entry<Integer, BSDSContent> entry : topicQueue.entrySet()) {
                            Integer key = entry.getKey();
                            BSDSContent msg = entry.getValue();
                            if (System.currentTimeMillis() - msg.getCurrentTimeMillis() > 30000) {
                                System.out.println("EXPIRED!!!!! " + msg.getMessage());
                                topicQueue.remove(key);
                                expiredCount++;
                            }
                        }
                    });
                }
            }
        }
        else {
            System.out.println("No topics in queue to monitor");
        }


//            execService.scheduleAtFixedRate(() -> {
//                for(Map.Entry<Integer,BSDSContent> entry : topicQueue.entrySet()) {
//                    Integer key = entry.getKey();
//                    BSDSContent msg = entry.getValue();
//                    if (System.currentTimeMillis() - msg.getCurrentTimeMillis() > 2000) {
//                        System.out.println("EXPIRED!!!!! " + msg.getMessage());
//                        topicQueue.remove(key);
//                        expiredCount ++;
//                    }
//                }
//            }, 10, 5, TimeUnit.SECONDS);


    }
}
