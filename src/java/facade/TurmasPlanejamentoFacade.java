package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.TurmasPlanejamento;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


@Stateless
public class TurmasPlanejamentoFacade extends AbstractFacade<TurmasPlanejamento>{
    
    public TurmasPlanejamentoFacade() {
        super(TurmasPlanejamento.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
//    public List<Disciplina> findByName(String nome){
//        Session session = getSessionFactory().openSession();
//        Criteria criteria = session.createCriteria(Disciplina.class);
//        criteria.add(Restrictions.eq("nome", nome));
//        
//        List results = criteria.list();
//        session.close();
//        
//        return results;
//        
//    }
//    
    public TurmasPlanejamento inicializarColecaoDisponibilidades(TurmasPlanejamento t){
        
        Session session = getSessionFactory().openSession();
        session.refresh(t);
        Hibernate.initialize(t);
        Hibernate.initialize(t.getDisponibilidades());
        session.close();
        return t;
        
    }
    
    
    
}


