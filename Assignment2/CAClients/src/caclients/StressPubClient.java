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

/**
 *
 * @author Amitash
 */
public class StressPubClient {
    
    public static void main(String[] args) {
        
        long start = 0;


        int numberOfMessages = (args.length < 1) ? 100 : Integer.parseInt(args[0]);
        int publisherThreads = (args.length < 2) ? 20 : Integer.parseInt(args[1]);
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
        
        String id4 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("sports"), String.class);
        System.out.println(id4);
        
        String id5 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("travel"), String.class);
        System.out.println(id5);
        
        String id6 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("travel"), String.class);
        System.out.println(id6);
        
        String id7 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("movies"), String.class);
        System.out.println(id7);
        
        String id8 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("movies"), String.class);
        System.out.println(id8);
        
        String id9 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("books"), String.class);
        System.out.println(id9);
        
        String id10 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("books"), String.class);
        System.out.println(id10);
        
        String id11 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("finance"), String.class);
        System.out.println(id11);
        
        String id12 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("finance"), String.class);
        System.out.println(id12);
        
        String id13 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("pets"), String.class);
        System.out.println(id13);
        
        String id14 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("pets"), String.class);
        System.out.println(id14);
        
        String id15 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("cats"), String.class);
        System.out.println(id15);
        
        String id16 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("cats"), String.class);
        System.out.println(id16);
        
        String id17 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("dogs"), String.class);
        System.out.println(id17);
        
        String id18 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("dogs"), String.class);
        System.out.println(id18);
        
        String id19 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("makeup"), String.class);
        System.out.println(id19);
        
        String id20 = myResource.request(MediaType.TEXT_PLAIN)
        .post(Entity.text("makeup"), String.class);
        System.out.println(id20);
        
        
        
        
        
        System.out.println("Completed publisher registration");
        start = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(publisherThreads);
        executorService.execute(new PublisherClientThread(id1,"news message from pub1",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id2,"news msg for pub2",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id3,"sports mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id4,"sports mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id5,"travel mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id6,"travel mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id7,"movies mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id8,"movies mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id9,"books mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id10,"books mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id11,"finance mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id12,"finance mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id13,"pets mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id14,"pets mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id15,"cats mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id16,"cats mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id17,"dogs mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id18,"dogs mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id19,"makeup mesg pub3",numberOfMessages,barrier));
        executorService.execute(new PublisherClientThread(id20,"makeup mesg pub3",numberOfMessages,barrier));
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
