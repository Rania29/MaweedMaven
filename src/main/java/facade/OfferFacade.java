/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.domain.Offer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Samir
 */
@Stateless
public class OfferFacade extends AbstractFacade<Offer> {

    @PersistenceContext(unitName = "MaweedPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OfferFacade() {
        super(Offer.class);
    }
    
}
