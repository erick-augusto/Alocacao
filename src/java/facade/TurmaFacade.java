/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.Turma;
import org.hibernate.SessionFactory;

/**
 *
 * @author Juliana
 */
@Stateless
public class TurmaFacade extends AbstractFacade<Turma>{
    
    public TurmaFacade() {
        super(Turma.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
    public Turma listByID(Long id){ //Problema para referenciar em turma controller
        
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Turma.class);
        criteria.add(Restrictions.eq("ID", id));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Turma> results = criteria.list();
        session.close();

        return results.get(0);
    }
}
