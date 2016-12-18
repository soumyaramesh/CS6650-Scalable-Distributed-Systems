/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.WordCountEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
public class WordCountStorageBean implements WordCountStorageBeanRemote {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public String getTopNWords(int n) {
        String qString = "SELECT we from WordCountEntity we "
                + "ORDER BY we.count DESC";
        Query query = em.createQuery(qString);
        query.setMaxResults(n);
        List<WordCountEntity> response = null;
        try {
         response = query.getResultList();
        }
        catch(Exception e) {
            System.out.println("Query failed");
            return null;
        }
        
        if(response == null || response.isEmpty()) {
            System.out.println("There's no words in the queue");
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        
        for(WordCountEntity we:response) {
            sb.append(we.getWord() + " : ");
            sb.append(we.getCount() + "\n");
            
        }
        
        return sb.toString();
       
        
    }
        
    
}
