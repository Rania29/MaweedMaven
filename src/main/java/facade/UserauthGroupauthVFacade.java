/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.domain.UserauthGroupauthV;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
<<<<<<< HEAD
 * @author Samir
=======
 * @author sawad
>>>>>>> 12a852bc2d0b5b5eae1bb8923890dc37105b12bb
 */
@Stateless
public class UserauthGroupauthVFacade extends AbstractFacade<UserauthGroupauthV> {

    @PersistenceContext(unitName = "MaweedPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserauthGroupauthVFacade() {
        super(UserauthGroupauthV.class);
    }
    
}
