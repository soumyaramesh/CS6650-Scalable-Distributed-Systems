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
import javax.ws.rs.QueryParam;
import beans.PublisherStorageBeanRemote;
import beans.SubscriberStorageBeanRemote;


@Stateless
@Path("subscriber")
public class SubscriberService {
    @EJB(beanName = "SubscriberStorageBean" ,mappedName = "ejb/SubscriberStorageBeanRemote", beanInterface = SubscriberStorageBeanRemote.class)
    private SubscriberStorageBeanRemote subscriberBean;
    
    @Path("registerSubscriber")
    @POST
    @Consumes("text/plain")
    public long registerSubscriber(@QueryParam("topic") String topic) {
        return subscriberBean.registerSubscriber(topic);
    }
}
