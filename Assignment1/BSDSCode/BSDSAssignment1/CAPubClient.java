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
        try {
            System.out.println ("Publisher Client Starter");
            Registry registry = LocateRegistry.getRegistry(host, 1099);
             System.out.println ("Connected to registry");
            BSDSPublishInterface CAServerStub = (BSDSPublishInterface) registry.lookup("CAServerPublisher");
            System.out.println ("Stub initialized");
            int id = CAServerStub.registerPublisher("Sporty Stuff", "Soccer");
            System.out.println("Pub id = " + Integer.toString(id));
            CAServerStub.publishContent(id, "Title", "Message", 30);
            System.out.println("Publish Client is working");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}    

