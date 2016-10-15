/**
 * Created by Ian Gortan on 9/19/2016.
 */
package BSDSAssignment1;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

//Skeleton of CAServer supporting both BSDS interfaces

public class CAServer implements BSDSPublishInterface, BSDSSubscribeInterface {

    private ConcurrentHashMap<String,String> publisherToTopic;
    private ConcurrentHashMap<String,String> subscriberToTopic;
    private ConcurrentHashMap<String,Queue<BSDSContent>> topicQueues;

    public CAServer(ConcurrentHashMap<String,String> publisherToTopic,
                    ConcurrentHashMap<String,String> subscriberToTopic,
                    ConcurrentHashMap<String,Queue<BSDSContent>> topicQueues) {
        this.publisherToTopic = publisherToTopic;
        this.subscriberToTopic = subscriberToTopic;
        this.topicQueues = topicQueues;
    }


    // registers a publisher and returns the id
    public synchronized String registerPublisher(String name, String topic) throws RemoteException {
        System.out.println("Publisher: " + name + topic);
        String pubId = UUID.randomUUID().toString();
        publisherToTopic.put(pubId,topic);
        if(!topicQueues.containsKey(topic)) {
            topicQueues.put(topic, new LinkedList<>());
        }
        return pubId;
    }


    // publishes a message to the server
    public synchronized void publishContent(String publisherID, String title, String message, int TimeToLive)
            throws RemoteException {
        String topic = publisherToTopic.get(publisherID);
        System.out.println(topic);
        if (!topicQueues.containsKey(topic)) {
            throw new NullPointerException("Could not find publisher for this topic");
        }
        else {
            topicQueues.get(topic).add(new BSDSContent(title, message, TimeToLive));
            System.out.println("Published message " + " Content: " +
                    title +
                    message
            );
        }
    }

    public String registerSubscriber(String topic) throws RemoteException {
        System.out.println("Topic is  " + topic);
        String subId = UUID.randomUUID().toString();
        subscriberToTopic.put(subId,topic);
        if(topicQueues.containsKey(topic) && !topicQueues.get(topic).isEmpty()) {
            for (BSDSContent msg : topicQueues.get(topic)) {
                msg.getSubscriberSet().add(subId);
            }
        }
        return subId;
    }


    // gets next outstanding message for a subscription
    public synchronized String getLatestContent(String subscriberID) throws RemoteException {
        String topic = subscriberToTopic.get(subscriberID);
        System.out.println("Size of topic queue " + topicQueues.size());
        String message = "";
        if(!topicQueues.containsKey(topic)) {
            System.out.println("There are no messages for this topic yet");
            return null;
        }
        System.out.println("There are messages");
            for (BSDSContent msg : topicQueues.get(topic)) {
                if (msg.getSubscriberSet().contains(subscriberID)) {
                    message = msg.getMessage();
                    msg.getSubscriberSet().remove(subscriberID);
                    System.out.println("returning latest content from queue on topic " + topic);
                    return message;
                }
            }

        System.out.println("queue is empty for topic" + topic);
        return null;
    }

    public static void main(String args[]) {
        try {
            ConcurrentHashMap<String,String> publisherToTopic = new ConcurrentHashMap<>();
            ConcurrentHashMap<String,String> subscriberToTopic = new ConcurrentHashMap<>();
            ConcurrentHashMap<String,Queue<BSDSContent>> topicQueues = new ConcurrentHashMap<>();
            CAServer objPub = new CAServer(publisherToTopic,subscriberToTopic,topicQueues);
            CAServer objSub = new CAServer(publisherToTopic,subscriberToTopic,topicQueues);
            System.out.println("Server Initializing");
            BSDSPublishInterface pStub = (BSDSPublishInterface) UnicastRemoteObject.exportObject(objPub, 0);
            BSDSSubscribeInterface sStub = (BSDSSubscribeInterface) UnicastRemoteObject.exportObject(objSub, 0);
            System.out.println("stubs created ....");
            // Bind the remote object's stub in the local host registry
            LocateRegistry.createRegistry(1099);

            Registry registry = LocateRegistry.getRegistry();
            System.out.println("Ref to Registry ok");
            try {
                registry.bind("CAServerPublisher", pStub);
                registry.bind("CAServerSubscriber", sStub);
            } catch (Exception e) {
                System.out.println("Caught already bound exception, probably safe to continue in dev mode" + e.toString());
            }
            System.err.println("CAServer ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
