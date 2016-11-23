/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment2WebTier;

import beans.WordCountStorageBeanRemote;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Stateless
@Path("/topN")
public class TopNWordsService {

    @EJB
    private WordCountStorageBeanRemote wordCountStorageBean;
    
    @GET
    @Produces
    public String getTopNWords(@QueryParam("n") int n) {
        return wordCountStorageBean.getTopNWords(n);
    }
 
}
