package BSDSAssignment1;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by soumya on 10/15/16.
 */
public interface BSDSDiagnosticsInterface extends Remote{

    HashMap<String,Integer> getNumberOfMessagesInQueue() throws RemoteException;

    int getNumberOfMessagesInQueue(String topic) throws RemoteException;
}
