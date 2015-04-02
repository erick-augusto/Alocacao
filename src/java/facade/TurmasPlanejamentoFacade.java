package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import model.TurmasPlanejamento;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@Stateless
public class TurmasPlanejamentoFacade extends AbstractFacade<TurmasPlanejamento>{
    
    public TurmasPlanejamentoFacade() {
        super(TurmasPlanejamento.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
    public TurmasPlanejamento inicializarColecaoDisponibilidades(TurmasPlanejamento t){
        
        Session session = getSessionFactory().openSession();
        session.refresh(t);
        Hibernate.initialize(t);
        Hibernate.initialize(t.getDisponibilidades());
        session.close();
        return t;
        
    }
    
    //Filtro por disciplina e/ou turno e/ou campus
    public List<TurmasPlanejamento> filtrarDTC(List<Disciplina> disciplinas, String turno, String campus) {

        try {
            List<TurmasPlanejamento> turmas = new ArrayList<>();

            Session session = getSessionFactory().openSession();
            

            if(!disciplinas.isEmpty()){
                
                for (Disciplina d : disciplinas) {

                    Criteria criteria = session.createCriteria(TurmasPlanejamento.class);
                    
                if (!campus.equals("")) {
                    criteria.add(Restrictions.eq("campus", campus));
                }

                if (!turno.equals("")) {
                    criteria.add(Restrictions.eq("turno", turno));
                }
                
//                    Query query = session.createQuery("from TurmasPlanejamento t where t.disciplina_disciplina_id = :id ");
//                    query.setParameter("id", d.getID());
                criteria.add(Restrictions.eq("disciplina", d));
                List resultado = criteria.list();
                
//                    List resultado = query.list();
                    turmas.addAll(resultado);
            }
            }
            
            else{
                
                Criteria criteria = session.createCriteria(TurmasPlanejamento.class);
                
                if (!campus.equals("")) {
                    criteria.add(Restrictions.eq("campus", campus));
                }

                if (!turno.equals("")) {
                    criteria.add(Restrictions.eq("turno", turno));
                }
                
                List resultado = criteria.list();
                turmas.addAll(resultado);
                
            }
            
            

            if (turmas.size() <= 0) {

                session.close();
                return null;

            } else {

                session.close();
                return turmas;

            }
        } catch (HibernateException e) {
            return null;
        }

    }
    
    
     //Filtro por disciplina e/ou turno e/ou campus e quadrimestre
    public List<TurmasPlanejamento> filtrarDTCQ(List<Disciplina> disciplinas, String turno, String campus, int quadrimestre) {

        try {
            List<TurmasPlanejamento> turmas = new ArrayList<>();

            Session session = getSessionFactory().openSession();
            

            if(!disciplinas.isEmpty()){
                
                for (Disciplina d : disciplinas) {

                    Criteria criteria = session.createCriteria(TurmasPlanejamento.class);
                    
                if (!campus.equals("")) {
                    criteria.add(Restrictions.eq("campus", campus));
                }

                if (!turno.equals("")) {
                    criteria.add(Restrictions.eq("turno", turno));
                }
                
//                    Query query = session.createQuery("from TurmasPlanejamento t where t.disciplina_disciplina_id = :id ");
//                    query.setParameter("id", d.getID());
                criteria.add(Restrictions.eq("disciplina", d));
                criteria.add(Restrictions.eq("quadrimestre", quadrimestre));
                List resultado = criteria.list();
                
//                    List resultado = query.list();
                    turmas.addAll(resultado);
            }
            }
            
            else{
                
                Criteria criteria = session.createCriteria(TurmasPlanejamento.class);
                
                if (!campus.equals("")) {
                    criteria.add(Restrictions.eq("campus", campus));
                }

                if (!turno.equals("")) {
                    criteria.add(Restrictions.eq("turno", turno));
                }
                
                criteria.add(Restrictions.eq("quadrimestre", quadrimestre));
                List resultado = criteria.list();
                turmas.addAll(resultado);
                
            }
            
            

            if (turmas.size() <= 0) {

                session.close();
                return null;

            } else {

                session.close();
                return turmas;

            }
        } catch (HibernateException e) {
            return null;
        }

    }
    
    //Lista as turmas por quadrimestre
    public List<TurmasPlanejamento> findAllQuad(int quadrimestre) {
        
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(TurmasPlanejamento.class);
        crit.add(Restrictions.eq("quadrimestre", quadrimestre));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//faz um select distinct

        //crit.setMaxResults(50);
        List results = crit.list();
        
        session.close();
        return results;
    }

}


