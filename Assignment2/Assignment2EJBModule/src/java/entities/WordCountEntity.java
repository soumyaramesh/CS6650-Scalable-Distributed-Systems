/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Amitash
 */
@Entity
public class WordCountEntity implements Serializable {

  
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="wc_entity_seq_gen", sequenceName="WC_ENTITY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wc_entity_seq_gen")
    private Long id;
    private String word;
    private int count;
    
    public WordCountEntity() {
        
    }
    
    public WordCountEntity(String word) {
        this.word = word;
        this.count = 1;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public void incrementCount() {
        this.count ++;
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
        if (!(object instanceof WordCountEntity)) {
            return false;
        }
        WordCountEntity other = (WordCountEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.WordCountEntity[ id=" + id + " ]";
    }
    
}
