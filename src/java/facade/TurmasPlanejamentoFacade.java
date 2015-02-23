package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.TurmasPlanejamento;
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
//    public Disciplina inicializarColecaoAfinidades(Disciplina d){
//        
//        Session session = getSessionFactory().openSession();
//        session.refresh(d);
//        Hibernate.initialize(d);
//        Hibernate.initialize(d.getAfinidades());
//        session.close();
//        return d;
//        
//    }
    
    
    
}


