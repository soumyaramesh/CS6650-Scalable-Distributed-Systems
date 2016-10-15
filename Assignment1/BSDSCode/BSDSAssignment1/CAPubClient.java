package BSDSAssignment1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ian Gortan
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

// Simple client to test publishing to CAServer over RMI

public class CAPubClient {

    private CAPubClient() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        int numberOfMessages = 10000;
        try {
            System.out.println ("Publisher Client Starter");
            Registry registry = LocateRegistry.getRegistry(host, 1099);
             System.out.println ("Connected to registry");
            BSDSPublishInterface CAServerStub = (BSDSPublishInterface) registry.lookup("CAServerPublisher");
            System.out.println ("Stub initialized");
            String id1 = CAServerStub.registerPublisher("Publisher1", "Soccer");
            System.out.println("Registered Pub id = " + id1);
            String id2 = CAServerStub.registerPublisher("Publisher2", "Soccer");
            System.out.println("Registered Pub id = " + id2);
            String id3 = CAServerStub.registerPublisher("Publisher3", "News");
            System.out.println("Registered Pub id = " + id3);

            for (int i = 1; i < 6; i++) {
                CAServerStub.publishContent(id1, "Title1", "Pub1SoccerMessage " + i, 30);
                CAServerStub.publishContent(id2, "Title2", "Pub2SoccerMessage " + i, 30);
                CAServerStub.publishContent(id3, "Title3", "NewsMessage" + i, 30);
            }

            System.out.println("Publish Client is working");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}    

