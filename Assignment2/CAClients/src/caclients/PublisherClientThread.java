/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caclients;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PublisherClientThread implements Runnable{
    
    
    private String pubId;
    private String message;
    private int maxMessages;
    private CyclicBarrier barrier;
    private String BASE_URL = "http://ec2-54-147-107-14.compute-1.amazonaws.com:8080/HelloWorld/resources/";
    private String resourceUrl = "message";

    public PublisherClientThread(String id, String message, int maxMessages, CyclicBarrier barrier) {
        this.pubId = id;
        this.message = message;
        this.maxMessages = maxMessages;
        this.barrier = barrier;
    }

    @Override
    public void run() { 
        Client client = ClientBuilder.newClient();
        
        try {
            for(int i=1;i<=maxMessages;i++) {
                WebTarget myResource = client.target(BASE_URL + resourceUrl).queryParam("publisherId", pubId);
                Response response = myResource.request(MediaType.TEXT_PLAIN).post(Entity.text(message));
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
    
}
