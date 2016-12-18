/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caclients;

import java.util.Random;
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
public class TopNClientThread implements Runnable {
    String BASE_URL = "http://ec2-54-147-107-14.compute-1.amazonaws.com:8080/HelloWorld/resources/";
    String topnUrl = "topN";
    
    

    @Override
    public void run() {
        while(true) {
         //To change body of generated methods, choose Tools | Templates.
            Random random = new Random();
            int n = random.nextInt(100);
            //int n = 10;
            System.out.println(n);
            Client client = ClientBuilder.newClient();
            WebTarget myResource2 = client.target(BASE_URL + topnUrl).queryParam("n", n);
            Response topN = myResource2.request(MediaType.TEXT_PLAIN).get();
            System.out.println(topN.readEntity(String.class));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TopNClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
