
/**
 * Created by Ian Gortan on 9/20/2016.
 */

package BSDSAssignment1;
import java.rmi.Remote;
import java.rmi.RemoteException;



public interface BSDSPublishInterface extends Remote{
    // returns unique publisherID. Each publisher publishes messaages on a single topic
    String registerPublisher(String name, String topic) throws RemoteException;

    // publishes a message to the server
    void publishContent (String publisherID,
                            String title,
                            String message,
                            int TimeToLive) throws RemoteException;
}
