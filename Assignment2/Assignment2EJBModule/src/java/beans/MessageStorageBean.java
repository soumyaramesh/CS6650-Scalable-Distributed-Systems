/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.MessageEntity;
import entities.PublisherEntity;
import entities.SubscriberEntity;
import entities.TopicEntity;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Amitash
 */
@Stateless(mappedName = "ejb/MessageStorageBeanRemote")
public class MessageStorageBean implements MessageStorageBeanRemote {
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory cf;
    @Resource(mappedName = "jms/Queue")
    private static Queue queue;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public void publishMessage(long pubId, String message) {
        PublisherEntity publisherEntity = (PublisherEntity) entityManager.find(PublisherEntity.class, pubId);
        String topic = publisherEntity.getTopic();
        MessageEntity messageEntity = new MessageEntity(message);
        TopicEntity topicEntity = (TopicEntity) entityManager.find(TopicEntity.class, topic);
        if(topicEntity == null) {
            System.out.println("Creating new queue for topic " + topic);
            TopicEntity newTopicEntity = new TopicEntity(topic);
            newTopicEntity.addMessage(messageEntity);
            entityManager.persist(newTopicEntity);
        }
        else {
            topicEntity.addMessage(messageEntity);
            entityManager.persist(topicEntity);
        }
    }
    
    @Override
    public String getLatestContent(long subId) {
        SubscriberEntity subscriberEntity = (SubscriberEntity) entityManager.find(SubscriberEntity.class,subId);
        String topic = subscriberEntity.getTopic();
        long lastSeenSeq = subscriberEntity.getLastSeenMessageId();
        TopicEntity topicEntity = (TopicEntity) entityManager.find(TopicEntity.class, topic);
        if(topicEntity == null) {
            System.out.println("No queue for this topic yet");
            return null;
        }
        
        String queryString = "SELECT message from "
                + "(SELECT messages from TopicEntity te WHERE te.topic = ?1)"
                + "WHERE message.Id = ?2";
        Query query = entityManager.createQuery(queryString);
        query.setParameter(1,topic);
        query.setParameter(2, lastSeenSeq+1);
        List response = query.getResultList();
        
        MessageEntity resultMessageEntity = null;
        if(response.isEmpty()) {
            System.out.println("No new messages in the queue");
            return null;
        }
       
        else {
            resultMessageEntity = (MessageEntity) response.get(0); 
        }
        System.out.println("Returning message from queue " + resultMessageEntity.getMessage());
        return resultMessageEntity.getMessage();
        
        
    }
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
