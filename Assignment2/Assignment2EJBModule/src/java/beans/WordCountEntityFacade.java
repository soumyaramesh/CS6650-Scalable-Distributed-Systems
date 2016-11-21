/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import entities.WordCountEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Amitash
 */
@Stateless
public class WordCountEntityFacade extends AbstractFacade<WordCountEntity> {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WordCountEntityFacade() {
        super(WordCountEntity.class);
    }
    
}
