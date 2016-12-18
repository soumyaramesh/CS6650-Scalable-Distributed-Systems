/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caclients;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amitash
 */
public class TopNClient {
    
    public static void main(String[] args) {
        
        
        try {
            
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            
            for(int i=0;i<20;i++) {    
                
                executorService.execute(new TopNClientThread());
            }
            
            System.out.println("Top N Client is working");
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(TopNClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
