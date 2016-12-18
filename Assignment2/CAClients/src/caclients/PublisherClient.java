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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class PublisherClient {
    public static void main(String[] args) {
        
        long start = 0;


        int numberOfMessages = (args.length < 1) ? 100 : Integer.parseInt(args[0]);
        int publisherThreads = (args.length < 2) ? 3 : Integer.parseInt(args[1]);
        System.out.println("Number of messages  = " + numberOfMessages);
        System.out.println("number of pub threads = " + publisherThreads);
        CyclicBarrier barrier = new CyclicBarrier(publisherThreads + 1);
        System.out.println ("Publisher Client Starter");
        
        
        String BASE_URL = "http://ec2-54-147-107-14.compute-1.amazonaws.com:8080/HelloWorld/resources/";
        String resourceUrl = "publisher";
        
        try {
        Client client = ClientBuilder.newClient();
        WebTarget myResource = client.target(BASE_URL + resourceUrl);
        
        String id1 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("news"), String.class);
        System.out.println(id1);
        
        String id2 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("news"), String.class);
        System.out.println(id2);
        
        String id3 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("sports"), String.class);
        System.out.println(id3);
        
        
        System.out.println("Completed publisher registration");
        start = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(publisherThreads);
        executorService.execute(new PublisherClientThread(id1,"news message from pub1",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id2,"news msg for pub2",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id3,"sports mesg pub3",numberOfMessages,barrier));
        System.out.println("Publisher Client is working");
        client.close();
        
        }
        catch(Exception e) {
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
