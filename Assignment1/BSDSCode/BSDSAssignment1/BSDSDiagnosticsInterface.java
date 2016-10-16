package BSDSAssignment1;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by soumya on 10/15/16.
 */
public interface BSDSDiagnosticsInterface extends Remote{

    int getNumberOfMessagesInQueue(String topic) throws RemoteException;
}
