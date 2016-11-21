/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment2WebTier;

import beans.PublisherSessionBeanRemote;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Amitash
 */
@Stateless
@Path("publisher")
public class PublisherService {

    @EJB
    private PublisherSessionBeanRemote publisherSessionBean;
    
    @Path("registerPublisher")
    @POST
    @Consumes("text/plain")
    public long registerPublisher(@QueryParam("topic") String topic) {
        return publisherSessionBean.registerPublisher(topic);
    }
}
