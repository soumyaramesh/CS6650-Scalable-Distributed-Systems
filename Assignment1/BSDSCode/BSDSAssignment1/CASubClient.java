/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BSDSAssignment1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;

/**
 *
 * @author Ian Gortan
 * Simple client to test subscribing from CAServer over RMI
 */
public class CASubClient {

    private CASubClient() {}

    public static void main(String[] args) {
        int subThreads = (args.length < 2) ? 10 : Integer.parseInt(args[1]);
        CyclicBarrier barrier = new CyclicBarrier(subThreads + 1);
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

            long start = System.nanoTime();

            ExecutorService executorService = Executors.newFixedThreadPool(subThreads);
            executorService.execute(new CASubClientThread(CAServerStub,id1,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id2,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id3,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id4,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id5,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id6,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id7,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id8,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id9,barrier));
            executorService.execute(new CASubClientThread(CAServerStub,id10,barrier));


            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            long time = System.nanoTime() - start;
            System.out.printf("Subscriber client Tasks took %.3f ms to run%n", time/1e6);

            System.out.println("Done!");
        } catch (Exception e) {
            System.err.println("Subscriber Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
} 
