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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Amitash
 */
class SubscriberClientThread implements Runnable {
    
    private String subId;
    private int count;
    private CyclicBarrier barrier;
    private Client client;
    
    private String BASE_URL = "http://ec2-54-147-107-14.compute-1.amazonaws.com:8080/HelloWorld/resources/";
    private String resourceUrl = "message";

    public SubscriberClientThread(String subId, CyclicBarrier barrier) {
        this.subId = subId;
        this.barrier = barrier;
    }   
    
    @Override
    public void run() {
        int n = 1;
        try {
            while(n<40) {
                client = ClientBuilder.newClient();
                WebTarget myResource = client.target(BASE_URL + resourceUrl).queryParam("subscriberId", subId);
                Response response = myResource.request(MediaType.TEXT_PLAIN).get();
                System.out.println("Response Status = " + response.getStatus());
                if(response.getStatus()== 200) {
                    String msg = response.readEntity(String.class);
                    count ++;
                    System.out.println("Received message = " + msg);
                    System.out.println("Total messages received so far by subscriber Thread " +Thread.currentThread() + " = " + count);
                    n = 1;
                }
                else {
                    System.out.println("sleep for " + n*100 + " ms");
                    try {
                        Thread.sleep(n * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    n = n*2;
                }
            }
            System.out.println("Timeout reached");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            client.close();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("Count at end of thread after timeout " + Thread.currentThread() + " = " + count);
    }

//    private Response getLatestContent(String subId) {
//        
//        Client client = ClientBuilder.newClient();
//        WebTarget myResource = client.target(BASE_URL + resourceUrl).queryParam("subscriberId", subId);
//        Response response = myResource.request(MediaType.TEXT_PLAIN).get();
//        System.out.println(response.getStatus());
//        return response;
//        
//        
//    }
    
}
