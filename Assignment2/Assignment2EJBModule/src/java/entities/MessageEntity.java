/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Amitash
 */
@Entity
public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="message_entity_seq_gen", sequenceName="MESSAGE_ENTITY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_entity_seq_gen")
    private Long id;
    private String message;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "topicEntity", referencedColumnName = "topic")
    private TopicEntity topicEntity;

//    public TopicEntity getTopicEntity() {
//        return topicEntity;
//    }
//
//    public void setTopicEntity(TopicEntity topicEntity) {
//        this.topicEntity = topicEntity;
//    }
    
    public MessageEntity() {
        
    }
    
    public MessageEntity(String messString) {
        this.message = messString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MessageEntity)) {
            return false;
        }
        MessageEntity other = (MessageEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.MessageEntity[ id=" + id + " ]";
    }
    
}
