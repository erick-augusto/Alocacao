package facade;

import controller.HibernateUtil;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@Stateless
public class DisciplinaFacade extends AbstractFacade<Disciplina>{
    
    public DisciplinaFacade() {
        super(Disciplina.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
    public List<Disciplina> findByName(String nome){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Disciplina.class);
        criteria.add(Restrictions.eq("nome", nome));
        
        List results = criteria.list();
        session.close();
        
        return results;
        
    }
    
    public Disciplina inicializarColecaoAfinidades(Disciplina d){
        
        Session session = getSessionFactory().openSession();
        session.refresh(d);
        Hibernate.initialize(d);
        Hibernate.initialize(d.getAfinidades());
        session.close();
        return d;
        
    }
    
    
    
}


