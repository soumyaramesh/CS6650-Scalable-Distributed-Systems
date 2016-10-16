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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;

// Simple client to test publishing to CAServer over RMI

public class CAPubClient {

    private CAPubClient() {}

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(20);

        String host = (args.length < 1) ? null : args[0];
        int numberOfMessages = 10000;
        long start = System.nanoTime();
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
            String id4 = CAServerStub.registerPublisher("Publisher4", "News");
            System.out.println("Registered Pub id = " + id4);
            String id5 = CAServerStub.registerPublisher("Publisher5", "Sports");
            System.out.println("Registered Pub id = " + id5);
            String id6 = CAServerStub.registerPublisher("Publisher6", "Sports");
            System.out.println("Registered Pub id = " + id6);
            String id7 = CAServerStub.registerPublisher("Publisher7", "Travel");
            System.out.println("Registered Pub id = " + id7);
            String id8 = CAServerStub.registerPublisher("Publisher8", "Travel");
            System.out.println("Registered Pub id = " + id8);
            String id9 = CAServerStub.registerPublisher("Publisher9", "Food");
            System.out.println("Registered Pub id = " + id9);
            String id10 = CAServerStub.registerPublisher("Publisher10", "Food");
            System.out.println("Registered Pub id = " + id10);
            String id11 = CAServerStub.registerPublisher("Publisher11", "Hiking");
            System.out.println("Registered Pub id = " + id11);
            String id12 = CAServerStub.registerPublisher("Publisher12", "Hiking");
            System.out.println("Registered Pub id = " + id12);
            String id13 = CAServerStub.registerPublisher("Publisher13", "Boston");
            System.out.println("Registered Pub id = " + id13);
            String id14 = CAServerStub.registerPublisher("Publisher14", "Boston");
            System.out.println("Registered Pub id = " + id14);
            String id15 = CAServerStub.registerPublisher("Publisher15", "Asia");
            System.out.println("Registered Pub id = " + id15);
            String id16 = CAServerStub.registerPublisher("Publisher16", "Asia");
            System.out.println("Registered Pub id = " + id16);
            String id17 = CAServerStub.registerPublisher("Publisher17", "Europe");
            System.out.println("Registered Pub id = " + id17);
            String id18 = CAServerStub.registerPublisher("Publisher18", "Europe");
            System.out.println("Registered Pub id = " + id18);
            String id19 = CAServerStub.registerPublisher("Publisher19", "Funny");
            System.out.println("Registered Pub id = " + id19);
            String id20 = CAServerStub.registerPublisher("Publisher20", "Funny");
            System.out.println("Registered Pub id = " + id20);

            Thread.sleep(10000);

            new Thread(new CAPubClientThread(CAServerStub, id1,"Title","SoccerMessage ", 30, numberOfMessages,barrier )).start();
            new Thread(new CAPubClientThread(CAServerStub, id2,"Title","SoccerMessage ", 30, numberOfMessages,barrier )).start();
            new Thread(new CAPubClientThread(CAServerStub, id3,"Title","NewsMessage ", 30, numberOfMessages,barrier )).start();
            new Thread(new CAPubClientThread(CAServerStub, id4,"Title","NewsMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id5,"Title","SportsMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id6,"Title","SportsMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id7,"Title","TravelMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id8,"Title","TravelMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id9,"Title","FoodMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id10,"Title","FoodMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id11,"Title","HikingMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id12,"Title","HikingMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id13,"Title","BostonMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id14,"Title","BostonMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id15,"Title","AsiaMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id16,"Title","AsiaMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id17,"Title","EuropeMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id18,"Title","EuropeMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id19,"Title","FunnyMessage", 30, numberOfMessages,barrier)).start();
            new Thread(new CAPubClientThread(CAServerStub, id20,"Title","FunnyMessage", 30, numberOfMessages,barrier)).start();
            System.out.println("Publish Client is working");
        } catch (Exception e) {
            System.err.println("Publisher Client exception: " + e.toString());
            e.printStackTrace();
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        long time = System.nanoTime() - start;
        System.out.printf("Publisher client Tasks took %.3f ms to run%n", time/1e6);

        System.out.println("Done!");
    }
}    

