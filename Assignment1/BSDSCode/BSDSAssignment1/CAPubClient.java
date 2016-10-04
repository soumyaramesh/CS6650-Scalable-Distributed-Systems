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

// Simple client to test publishing to CAServer over RMI

public class CAPubClient {

    private CAPubClient() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        int numberOfMessages = Integer.parseInt(args[1]);
        try {
            System.out.println ("Publisher Client Starter");
            Registry registry = LocateRegistry.getRegistry(host, 1099);
             System.out.println ("Connected to registry");
            BSDSPublishInterface CAServerStub = (BSDSPublishInterface) registry.lookup("CAServerPublisher");
            System.out.println ("Stub initialized");
            int id1 = CAServerStub.registerPublisher("Publisher1", "Soccer");
            System.out.println("Registered Pub id = " + Integer.toString(id1));
            int id2 = CAServerStub.registerPublisher("Publisher2", "Soccer");
            System.out.println("Registered Pub id = " + Integer.toString(id2));
            int id3 = CAServerStub.registerPublisher("Publisher3", "News");
            System.out.println("Registered Pub id = " + Integer.toString(id3));

            for (int i = 0; i < numberOfMessages; i++) {
                CAServerStub.publishContent(id1, "Title", "Message", 30);
                CAServerStub.publishContent(id2, "Title", "Message", 30);
                CAServerStub.publishContent(id3, "Title", "Message", 30);
            }

            System.out.println("Publish Client is working");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}    

