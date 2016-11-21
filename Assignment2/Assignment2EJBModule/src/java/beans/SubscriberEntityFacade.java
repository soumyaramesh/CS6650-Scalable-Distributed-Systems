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

/**
 *
 * @author Amitash
 */
@Stateless
public class SubscriberEntityFacade extends AbstractFacade<SubscriberEntity> {

    @PersistenceContext(unitName = "Assignment2EJBModulePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SubscriberEntityFacade() {
        super(SubscriberEntity.class);
    }
    
}
