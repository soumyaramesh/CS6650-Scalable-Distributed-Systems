/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Amitash
 */
@Entity
public class TopicEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String topic;
    @OneToMany(mappedBy = "topicEntity", cascade = CascadeType.PERSIST)
    private List<MessageEntity> messages;
    
    public TopicEntity() {
        
    }
    
    public TopicEntity(String topic) {
        this.topic = topic;
        this.messages = new ArrayList<>();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
    
    public void addMessage(MessageEntity messageEntity) {
        messages.add(messageEntity);
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (topic != null ? topic.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TopicEntity)) {
            return false;
        }
        TopicEntity other = (TopicEntity) object;
        if ((this.topic == null && other.topic != null) || (this.topic != null && !this.topic.equals(other.topic))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TopicEntity[ id=" + topic + " ]";
    }
    
}
