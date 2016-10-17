/**
 * Created by Ian Gortan on 9/19/2016.
 */
package BSDSAssignment1;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.*;

//Skeleton of CAServer supporting both BSDS interfaces

public class CAServer implements BSDSPublishInterface, BSDSSubscribeInterface, BSDSDiagnosticsInterface {

    private ConcurrentHashMap<String, String> publisherToTopic;
    private ConcurrentHashMap<String, String> subscriberToTopic;
    private ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, BSDSContent>> topicQueues;
    private ConcurrentHashMap<String, Integer> topicToLastSeqNo;
    private ConcurrentHashMap<String, Integer> subscriberToLastSeenSeqNo;
    private ConcurrentHashMap<String, Integer> topicToSubscriberCount;

    public CAServer(ConcurrentHashMap<String, String> publisherToTopic,
                    ConcurrentHashMap<String, String> subscriberToTopic,
                    ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, BSDSContent>> topicQueues,
                    ConcurrentHashMap<String, Integer> topicToLastSeqNo,
                    ConcurrentHashMap<String, Integer> subscriberToLastSeenSeqNo,
                    ConcurrentHashMap<String, Integer> topicToSubscriberCount) {
        this.publisherToTopic = publisherToTopic;
        this.subscriberToTopic = subscriberToTopic;
        this.topicQueues = topicQueues;
        this.topicToLastSeqNo = topicToLastSeqNo;
        this.subscriberToLastSeenSeqNo = subscriberToLastSeenSeqNo;
        this.topicToSubscriberCount = topicToSubscriberCount;
    }


    // registers a publisher and returns the id
    public synchronized String registerPublisher(String name, String topic) throws RemoteException {
        System.out.println("Publisher: " + name + topic);
        String pubId = UUID.randomUUID().toString();
        publisherToTopic.put(pubId, topic);
        if (!topicQueues.containsKey(topic)) {
            topicQueues.put(topic, new ConcurrentSkipListMap<>());
        }
        topicToLastSeqNo.put(topic, 0);
        topicToSubscriberCount.put(topic, 0);
        return pubId;
    }


    // publishes a message to the server
    public synchronized void publishContent(String publisherID, String title, String message, int TimeToLive)
            throws RemoteException {
        String topic = publisherToTopic.get(publisherID);
        ConcurrentSkipListMap<Integer, BSDSContent> topicQueue = topicQueues.get(topic);
        int newSeqNo = topicToLastSeqNo.get(topic) + 1;
        topicQueue.put(newSeqNo, new BSDSContent(title, message, TimeToLive, 0, System.currentTimeMillis()));
        topicToLastSeqNo.put(topic, newSeqNo);
        System.out.println("Published message " + newSeqNo + " Content: " +
                title +
                message
        );

    }

    public String registerSubscriber(String topic) throws RemoteException {
        String subId = UUID.randomUUID().toString();
        subscriberToTopic.put(subId, topic);
        subscriberToLastSeenSeqNo.put(subId, 0);
        int subCount = topicToSubscriberCount.get(topic);
        topicToSubscriberCount.put(topic, subCount + 1);
        System.out.println("Registered subscriber to Topic " + topic);
        return subId;
    }


    // gets next outstanding message for a subscription
    public synchronized String getLatestContent(String subscriberID) throws RemoteException {
        String msg = "";
        String topic = subscriberToTopic.get(subscriberID);
        if (topic == null) {
            throw new RemoteException("Couldn't find topic for this subscriber ID");
        }
        int lastSeenSeq = subscriberToLastSeenSeqNo.get(subscriberID);
        System.out.println("Size of topic queue for topic " + topic + "is " + topicQueues.get(topic).size());
        if (!topicQueues.containsKey(topic)) {
            System.out.println("There are no messages published for this topic yet");
            return null;
        }
        ConcurrentSkipListMap<Integer, BSDSContent> topicQueue = topicQueues.get(topic);
        if (topicQueue.ceilingKey(lastSeenSeq) == null) {
            System.out.println("No more new messages for this topic yet. Ask again later");
            return null;
        } else {

            try {
                int curMessageSeq = topicQueue.ceilingKey(lastSeenSeq);
                BSDSContent nextMessage = topicQueue.get(curMessageSeq);
                int updatedDeliveredCount = nextMessage.getDeliveredCount() + 1;
                if (updatedDeliveredCount == topicToSubscriberCount.get(topic)) {
                    System.out.println("*********** Deleting message from server ***********");
                    topicQueue.remove(curMessageSeq);
                    topicQueues.put(topic, topicQueue);
                } else {
                    nextMessage.setDeliveredCount(updatedDeliveredCount);
                    topicQueue.put(curMessageSeq, nextMessage);
                    topicQueues.put(topic, topicQueue);
                }
                subscriberToLastSeenSeqNo.put(subscriberID, curMessageSeq);
                msg = nextMessage.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("returning latest content from queue on topic " + topic);

            return msg;
        }
    }

    @Override
    public synchronized HashMap<String, Integer> getNumberOfMessagesInQueue() throws RemoteException {
        HashMap<String, Integer> topicQueueLen = new HashMap<>();
        for (String topic : topicQueues.keySet()) {
            topicQueueLen.put(topic, topicQueues.get(topic).size());
        }
        return topicQueueLen;
    }

    @Override
    public synchronized int getNumberOfMessagesInQueue(String topic) throws RemoteException {
        return topicQueues.get(topic).size();
    }


    public static void main(String args[]) {
        try {
            ConcurrentHashMap<String, String> publisherToTopic = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, String> subscriberToTopic = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, BSDSContent>> topicQueues = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> topicToLastSeqNo = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> subscriberToLastSeenSeqNo = new ConcurrentHashMap<>();
            ConcurrentHashMap<String, Integer> topicToSubscriberCount = new ConcurrentHashMap<>();

            CAServer objPub = new CAServer(publisherToTopic, subscriberToTopic, topicQueues,
                    topicToLastSeqNo, subscriberToLastSeenSeqNo, topicToSubscriberCount);
            CAServer objSub = new CAServer(publisherToTopic, subscriberToTopic, topicQueues,
                    topicToLastSeqNo, subscriberToLastSeenSeqNo, topicToSubscriberCount);
            CAServer objDiagnostics = new CAServer(publisherToTopic, subscriberToTopic, topicQueues,
                    topicToLastSeqNo, subscriberToLastSeenSeqNo, topicToSubscriberCount);

            System.out.println("Server Initializing");
            BSDSPublishInterface pStub = (BSDSPublishInterface) UnicastRemoteObject.exportObject(objPub, 0);
            BSDSSubscribeInterface sStub = (BSDSSubscribeInterface) UnicastRemoteObject.exportObject(objSub, 0);
            BSDSDiagnosticsInterface dStub = (BSDSDiagnosticsInterface) UnicastRemoteObject.exportObject(objDiagnostics,0);
            System.out.println("stubs created ....");
            // Bind the remote object's stub in the local host registry
            LocateRegistry.createRegistry(1099);

            Registry registry = LocateRegistry.getRegistry();
            System.out.println("Ref to Registry ok");
            try {
                registry.bind("CAServerPublisher", pStub);
                registry.bind("CAServerSubscriber", sStub);
                registry.bind("CAServerDiagnostics", dStub);
            } catch (Exception e) {
                System.out.println("Caught already bound exception, probably safe to continue in dev mode" + e.toString());
            }
            System.err.println("CAServer ready");

            ScheduledExecutorService execService = Executors.newSingleThreadScheduledExecutor();
            execService.scheduleAtFixedRate(new DeleteExpiredMessagesThread(topicQueues),5,1,TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
