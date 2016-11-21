/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment2WebTier;

import beans.MessageStorageBeanRemote;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

/**
 *
 * @author Amitash
 */
@Stateless
@Path("message")
public class MessageService {

    @EJB
    private MessageStorageBeanRemote messageStorageBean;
    
    @Path("publishContent")
    @POST
    @Consumes("text/plain")
    public void publishContent(@QueryParam("publisherId") long publisherId,
            @QueryParam("message") String message) {
        messageStorageBean.publishMessage(publisherId, message);
    }
    
    @Path("getLatestContent")
    @GET
    @Consumes("text/plain")
    public String getLatestContent(@QueryParam("subscriberId") long subscriberId) {
        return messageStorageBean.getLatestContent(subscriberId);
    }
    
    
}
