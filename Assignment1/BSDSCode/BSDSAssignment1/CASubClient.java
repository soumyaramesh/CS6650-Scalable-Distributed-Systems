/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSDSAssignment1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Ian Gortan
 * Simple client to test subscribing from CAServer over RMI
 */
public class CASubClient {

    private CASubClient() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            System.out.println ("Publisher Client Starter");
            Registry registry = LocateRegistry.getRegistry(host);
             System.out.println ("Connected to registry");
            BSDSSubscribeInterface CAServerStub = (BSDSSubscribeInterface) registry.lookup("CAServerSubscriber");
            System.out.println ("Stub initialized");
            int id = CAServerStub.registerSubscriber("Soccer");
            System.out.println("Pub id = " + Integer.toString(id));
            String message = CAServerStub.getLatestContent(id);
            
            System.out.println("message is " + message);
            System.out.println (" ... Looks like Subscribe Client is working too");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
} 
