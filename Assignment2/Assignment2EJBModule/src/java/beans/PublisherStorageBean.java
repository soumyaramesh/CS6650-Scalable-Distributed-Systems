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

/**
 *
 * @author Amitash
 */
@Stateless
public class PublisherStorageBean implements PublisherSessionBeanRemote {
    
    @PersistenceContext
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

    @Override
    public void publishContent(String publisherId, String title, String message) {
        
    }
    
}
