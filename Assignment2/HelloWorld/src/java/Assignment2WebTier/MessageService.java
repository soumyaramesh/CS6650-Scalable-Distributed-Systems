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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Stateless
@Path("/message")
public class MessageService {

    @EJB
    private MessageStorageBeanRemote messageStorageBean;
    
    
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public void publishContent(@DefaultValue("1") @QueryParam("publisherId") long publisherId, String message) {
        messageStorageBean.publishMessage(publisherId, message);
    }
    
    
    @GET
    @Consumes("text/plain")
    @Produces("text/plain")
    public String getLatestContent(@DefaultValue("1") @QueryParam("subscriberId") long subscriberId) {
        return messageStorageBean.getLatestContent(subscriberId);
    }
    
    
}
