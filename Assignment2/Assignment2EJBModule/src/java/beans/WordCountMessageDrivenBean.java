/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.WordCountEntity;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amitash
 */
@JMSDestinationDefinition(name = "java:app/MessageQueue", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "MessageQueue")
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/MessageQueue")
})
public class WordCountMessageDrivenBean implements MessageListener {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager em;
    
    public WordCountMessageDrivenBean() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            String body = message.getBody(String.class);
            System.out.println("In message driven bean");
            System.out.println(body);
            String[] arr = body.split(" ");
            List<String> wordList = Arrays.asList(arr);
            for(String word:wordList) {
                WordCountEntity we = (WordCountEntity) em.find(WordCountEntity.class, word);
                if(we == null) {
                    System.out.println("New word created");
                    WordCountEntity newWordCountEntity = new WordCountEntity(word);
                    em.persist(newWordCountEntity);
                }
                else {
                    System.out.println(word + " :Incrementing word count from " + we.getCount());
                    we.incrementCount();
                    System.out.println("To " + we.getCount());
                    em.persist(we);
                }
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(WordCountMessageDrivenBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void persist(Object object) {
        em.persist(object);
    }
    
}
