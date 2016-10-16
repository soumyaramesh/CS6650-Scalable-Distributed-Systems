package BSDSAssignment1;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by soumya on 10/16/16.
 */
public class CADiagnosticsClient {

    public CADiagnosticsClient() {

    }

    public static void main(String args[]) {
        String host = (args.length < 1) ? null : args[0];
        try {
            System.out.println ("Diagnostics Client Starter");
            Registry registry = LocateRegistry.getRegistry(host);
            System.out.println ("Connected to registry");
            BSDSDiagnosticsInterface CAServerStub = (BSDSDiagnosticsInterface) registry.lookup("CAServerDiagnostics");
            System.out.println ("Stub initialized");
            ScheduledExecutorService execService = Executors.newScheduledThreadPool(1);
            execService.scheduleAtFixedRate(() -> {
                HashMap<String,Integer> topicQueueLen = null;
                try {
                    topicQueueLen = CAServerStub.getNumberOfMessagesInQueue();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Iterator it = topicQueueLen.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    it.remove();
                }
            }, 0,5, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            System.err.println("Diagnostics Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
