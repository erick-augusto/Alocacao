package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.Afinidade;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


@Stateless
public class AfinidadeFacade extends AbstractFacade<Afinidade>{
    
    public AfinidadeFacade() {
        super(Afinidade.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    

    public Afinidade findByIDs(Long disciplinaId, Long pessoaId){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Afinidade.class);
            criteria.add(Restrictions.eq("disciplinaId", disciplinaId));
            criteria.add(Restrictions.eq("pessoaId", pessoaId));
            List resultado = criteria.list();
            

            if (resultado.size() <= 0) {
                session.close();
                return null;
            } else {
                Afinidade a = (Afinidade) resultado.get(0);
                session.close();
                return a;
            }
        } catch (HibernateException e) {
            return null;
        }
        
        
        
    }
    
    @Override
    public void remove(Afinidade a) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(a);
        try {
            transaction.commit();
        } finally {
            session.close();
        }
    }
    
  
    
    
    
}


