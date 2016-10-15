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
        int nmcount = 0;
        int smcount = 0;
        String host = (args.length < 1) ? null : args[0];
        try {
            System.out.println ("Subsriber Client Starter");
            Registry registry = LocateRegistry.getRegistry(host);
             System.out.println ("Connected to registry");
            BSDSSubscribeInterface CAServerStub = (BSDSSubscribeInterface) registry.lookup("CAServerSubscriber");
            System.out.println ("Stub initialized");
            String id1 = CAServerStub.registerSubscriber("Soccer");
            System.out.println("Sub id = " + id1);
            String id2 = CAServerStub.registerSubscriber("News");
            System.out.println("Sub id = " + id2);
            Thread th1 = new Thread(new CASubClientThread(CAServerStub,id1,smcount));
            Thread th2 = new Thread(new CASubClientThread(CAServerStub,id2,nmcount));
            th1.start();
            th2.start();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
} 
