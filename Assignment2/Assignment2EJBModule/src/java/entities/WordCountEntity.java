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

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (word != null ? word.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WordCountEntity)) {
            return false;
        }
        WordCountEntity other = (WordCountEntity) object;
        if ((this.word == null && other.word != null) || (this.word != null && !this.word.equals(other.word))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.WordCountEntity[ id=" + word + " ]";
    }
    
}
