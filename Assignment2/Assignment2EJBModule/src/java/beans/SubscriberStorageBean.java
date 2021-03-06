/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.SubscriberEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class SubscriberStorageBean implements SubscriberStorageBeanRemote {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager entityManager;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    

    @Override
    public long registerSubscriber(String topic) {
        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setTopic(topic);
        subscriberEntity.setLastSeenMessageId(Long.MIN_VALUE);
        entityManager.persist(subscriberEntity);
        return subscriberEntity.getId();
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }
}
