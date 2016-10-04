/**
 * Created by Ian Gortan on 9/19/2016.
 */
package BSDSAssignment1;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

//Skeleton of CAServer supporting both BSDS interfaces

public class CAServer implements BSDSPublishInterface, BSDSSubscribeInterface {

    HashMap<Integer,String> publisherToTopic = new HashMap<>();
    HashMap<Integer,String> subscriberToTopic = new HashMap<>();
    HashMap<String,Queue<BSDSContent>> topicQueues = new HashMap<>();


    // registers a publisher and returns the id
    public int registerPublisher(String name, String topic) throws RemoteException {
        System.out.println("Publisher: " + name + topic);
        int pubId = Integer.parseInt(UUID.randomUUID().toString());
        publisherToTopic.put(pubId,topic);
        return pubId;
    }


    // publishes a message to the server
    public void publishContent(int publisherID, String title, String message, int TimeToLive)
            throws RemoteException {
        String topic = publisherToTopic.get(publisherID);
        if (topicQueues.get(topic).isEmpty()) {
            throw new NullPointerException("Could not find publisher for this topic");
        }
        else {
            topicQueues.get(topic).add(new BSDSContent(title, message, TimeToLive));
            System.out.println("Message:" +
                    title +
                    message
            );
        }
    }

    public int registerSubscriber(String topic) throws RemoteException {
        System.out.println("Topic is  " + topic);
        Integer subId = Integer.parseInt(UUID.randomUUID().toString());
        subscriberToTopic.put(subId,topic);
        return subId;
    }


    // gets next outstanding message for a subscription
    public String getLatestContent(int subscriberID) throws RemoteException {
        String message = "Title: Soccer scores, Message: City win 8 straight games";
        System.out.println("returning latest content");
        return message;
    }

    public static void main(String args[]) {
        try {
            CAServer objPub = new CAServer();
            CAServer objSub = new CAServer();
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
