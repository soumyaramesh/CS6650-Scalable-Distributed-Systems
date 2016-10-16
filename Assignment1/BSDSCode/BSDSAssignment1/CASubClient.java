/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSDSAssignment1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Ian Gortan
 * Simple client to test subscribing from CAServer over RMI
 */
public class CASubClient {

    private CASubClient() {}

    public static void main(String[] args) {
        CyclicBarrier barrier1 = new CyclicBarrier(10);
        int count = 0;
        String host = (args.length < 1) ? null : args[0];
        try {
            System.out.println ("Subscriber Client Starter");
            Registry registry = LocateRegistry.getRegistry(host);
             System.out.println ("Connected to registry");
            BSDSSubscribeInterface CAServerStub = (BSDSSubscribeInterface) registry.lookup("CAServerSubscriber");
            System.out.println ("Stub initialized");
            String id1 = CAServerStub.registerSubscriber("Soccer");
            System.out.println("Sub id = " + id1);
            String id2 = CAServerStub.registerSubscriber("News");
            System.out.println("Sub id = " + id2);
            String id3 = CAServerStub.registerSubscriber("Sports");
            System.out.println("Sub id = " + id3);
            String id4 = CAServerStub.registerSubscriber("Travel");
            System.out.println("Sub id = " + id4);
            String id5 = CAServerStub.registerSubscriber("Food");
            System.out.println("Sub id = " + id5);
            String id6 = CAServerStub.registerSubscriber("Hiking");
            System.out.println("Sub id = " + id6);
            String id7 = CAServerStub.registerSubscriber("Boston");
            System.out.println("Sub id = " + id7);
            String id8 = CAServerStub.registerSubscriber("Asia");
            System.out.println("Sub id = " + id8);
            String id9 = CAServerStub.registerSubscriber("Europe");
            System.out.println("Sub id = " + id9);
            String id10 = CAServerStub.registerSubscriber("Funny");
            System.out.println("Sub id = " + id10);


            new Thread(new CASubClientThread(CAServerStub,id1,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id2,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id3,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id4,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id5,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id6,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id7,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id8,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id9,count,barrier1)).start();
            new Thread(new CASubClientThread(CAServerStub,id10,count,barrier1)).start();
        } catch (Exception e) {
            System.err.println("Subscriber Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
} 
