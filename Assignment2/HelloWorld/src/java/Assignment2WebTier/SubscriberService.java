/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment2WebTier;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import beans.SubscriberStorageBeanRemote;

@Stateless
@Path("/subscriber")
public class SubscriberService { 
    @EJB
    private SubscriberStorageBeanRemote subscriberBean;
            
    @POST
    @Consumes("text/plain")
    public long registerSubscriber(String topic) {
        return subscriberBean.registerSubscriber(topic);
    }
   
}
