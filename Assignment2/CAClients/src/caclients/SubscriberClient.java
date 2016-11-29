/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caclients;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Amitash
 */
public class SubscriberClient {
    
    public static void main(String[] args) {
        int subThreads = (args.length < 1) ? 2 : Integer.parseInt(args[0]);
        System.out.println("Subscriber Threads = " + subThreads);
        CyclicBarrier barrier = new CyclicBarrier(3);
        System.out.println ("Subscriber Client Starter");
        long start = 0;
            
        String BASE_URL = "http://ec2-54-147-107-14.compute-1.amazonaws.com:8080/HelloWorld/resources/";
        String resourceUrl = "subscriber";
        String topnUrl = "topN";
        
        try {
            Client client = ClientBuilder.newClient();
            WebTarget myResource = client.target(BASE_URL + resourceUrl);

            String id1 = myResource.request(MediaType.TEXT_PLAIN)
            .post(Entity.text("news"), String.class);
            System.out.println(id1);
            
        
            String id2 = myResource.request(MediaType.TEXT_PLAIN)
            .post(Entity.text("sports"), String.class);
            System.out.println(id2);
            
            System.out.println("Completed subscriber registration");
            start = System.nanoTime();
            ExecutorService executorService = Executors.newFixedThreadPool(subThreads);
            executorService.execute(new SubscriberClientThread(id1, barrier));
            executorService.execute(new SubscriberClientThread(id2,barrier));
            System.out.println("Subscriber Client is working");
            client.close();
            
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            
            long time = System.nanoTime() - start;
            System.out.printf("Subscriber client Tasks took %.3f ms to run%n", time/1e6);
            
            Client client2 = ClientBuilder.newClient();
            WebTarget myResource2 = client2.target(BASE_URL + topnUrl).queryParam("n", 10);

            Response topN = myResource2.request(MediaType.TEXT_PLAIN).get();
            System.out.println(topN.readEntity(String.class));
            
            System.out.println("Done!");
        } catch (Exception e) {
            System.err.println("Subscriber Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    
}
