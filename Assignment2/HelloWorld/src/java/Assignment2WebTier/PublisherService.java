/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment2WebTier;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import beans.PublisherStorageBeanRemote;
import javax.ejb.Stateless;

@Stateless
@Path("/publisher")
public class PublisherService {

    @EJB
    private PublisherStorageBeanRemote publisherSessionBean;
    
    @POST
    @Consumes("text/plain")
    public long registerPublisher(String topic) {
        return publisherSessionBean.registerPublisher(topic);
    }
}
