/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.PublisherEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PublisherStorageBean implements PublisherStorageBeanRemote {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager entityManager;
    
    

    @Override
    public long registerPublisher(String topic) {
        PublisherEntity publisherEntity = new PublisherEntity();
        publisherEntity.setTopic(topic);
        entityManager.persist(publisherEntity);
        return publisherEntity.getId();
        
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        entityManager.persist(object);
    }

    
}
