/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.MessageEntity;
import entities.PublisherEntity;
import entities.SubscriberEntity;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MessageStorageBean implements MessageStorageBeanRemote {

    @Resource(mappedName = "java:app/MessageQueue")
    private Queue java_appMessageQueue;

    @Inject
    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    private JMSContext context;
    
    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager entityManager;
    
    
 
//    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
//    private ConnectionFactory cf;
//    @Resource(mappedName = "jms/Queue")
//    private static Queue queue;
    
    
    @Override
    public void publishMessage(long pubId, String message) {
        System.out.println("Publisher id is " + pubId);
        PublisherEntity publisherEntity = (PublisherEntity) entityManager.find(PublisherEntity.class, pubId);
        if(publisherEntity == null) {
            System.out.println("Could not find publisher Id");
        }
        else {
            String topic = publisherEntity.getTopic();
            System.out.println("Publisher's topic is " + topic);
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMessage(message);
            messageEntity.setTopic(topic);
            entityManager.persist(messageEntity);
            System.out.println("Succesfully published msg " + message);
            sendJMSMessageToMessageQueue(message);
        }
    }
    
    @Override
    public String getLatestContent(long subId) {
        System.out.println("Getting latest content for subId " + subId);
        SubscriberEntity subscriberEntity = (SubscriberEntity) entityManager.find(SubscriberEntity.class,subId);
        if(subscriberEntity == null) {
            System.out.println("Could not find subId");
            return null;
        }
        String topic = subscriberEntity.getTopic();
        System.out.println("Subscriber's topic is " + topic);
        long lastSeenSeq = subscriberEntity.getLastSeenMessageId();
        System.out.println("Subscriber's last seen msg id = "  + lastSeenSeq);
       
        String queryString = "SELECT me "
                + "FROM MessageEntity me "
                + "WHERE me.topic = ?1 "
                + "AND me.id > ?2 "
                + "ORDER BY me.id";
        Query query = entityManager.createQuery(queryString);
        query.setParameter(1,topic);
        query.setParameter(2, lastSeenSeq);
        
        List response = null;
        try {
         response = query.getResultList();
        }
        catch(Exception e) {
            System.out.println("Query failed or There's no messages at all for this topic");
            return null;
        }
        
        if(response == null || response.isEmpty()) {
            System.out.println("There's no messages at all for this topic");
            return null;
        }
        
        System.out.println("size of topic queue for topic " + topic + "is "+ response.size());
        MessageEntity result = (MessageEntity) response.get(0);
        
        if(result == null) {
            System.out.println("Couldn't find a new message");
            return null;
        }
        
        long latestMsgId = result.getId();
        System.out.println("Delivered message from queue " + result.getMessage());
        System.out.println(latestMsgId);
        
        subscriberEntity.setLastSeenMessageId(latestMsgId);
        entityManager.persist(subscriberEntity);
        return result.getMessage();
        
    }
    
    
    
   
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        entityManager.persist(object);
    }



    private void sendJMSMessageToMessageQueue(String messageData) {
        
        context.createProducer().send(java_appMessageQueue, messageData);
        
    }


    
}
