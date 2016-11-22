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


@Stateless
public class MessageStorageBean implements MessageStorageBeanRemote {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager entityManager;
//    @Resource(mappedName = "jms/ConnectionFactory")
//    private static ConnectionFactory cf;
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
            entityManager.persist(messageEntity);
            TopicEntity topicEntity = (TopicEntity) entityManager.find(TopicEntity.class, topic);
            if(topicEntity == null) {
                System.out.println("Creating new queue for topic " + topic);
                TopicEntity newTopicEntity = new TopicEntity(topic);
                //messageEntity.setTopicEntity(newTopicEntity);
                newTopicEntity.addMessage(messageEntity);
                entityManager.persist(newTopicEntity);
            }
            else {
                System.out.println("Found existing topic queue for topic " + topic);
                System.out.println(topicEntity);
                //messageEntity.setTopicEntity(topicEntity);
                topicEntity.addMessage(messageEntity);
                entityManager.persist(topicEntity);
            }
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
        TopicEntity topicEntity = (TopicEntity) entityManager.find(TopicEntity.class, topic);
        if(topicEntity == null) {
            System.out.println("No queue for this topic yet " + topic);
            return null;
        }
        
//        String queryString = "SELECT message from "
//                + "(SELECT messages from TopicEntity te WHERE te.topic = ?1) "
//                + "WHERE message.Id = ?2";
        System.out.println("Found topic queue for this topic " + topic);
        String queryString1 = "SELECT te.messages FROM TopicEntity te WHERE te.topic = ?1";
        Query query1 = entityManager.createQuery(queryString1,TopicEntity.class);
        //Query query = entityManager.createQuery(queryString);
        query1.setParameter(1,topic);
        
        
        //query.setParameter(2, lastSeenSeq+1);
        List response = query1.getResultList();
        
        if(response == null || response.isEmpty()) {
            System.out.println("Here comes trouble. Queue empty for this topic");
            return null;
        }
        System.out.println(response);
        System.out.println(response.size());
        MessageEntity resultMessageEntity = null;
//        for((MessageEntity) me:response) {
//            if(me.getId().equals(lastSeenSeq+1))
//                resultMessageEntity = me;
//        }
        
        
        
        if(resultMessageEntity == null) {
            System.out.println("No new messages in the queue");
            return null;
        }
       
        else {
            System.out.println("Returning message from queue " + resultMessageEntity.getMessage());
            return resultMessageEntity.getMessage();
        }
        
        
    }
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        entityManager.persist(object);
    }
    
}
