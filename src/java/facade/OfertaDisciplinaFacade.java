package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import model.OfertaDisciplina;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@Stateless
public class OfertaDisciplinaFacade extends AbstractFacade<OfertaDisciplina>{
    
    public OfertaDisciplinaFacade() {
        super(OfertaDisciplina.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
    public OfertaDisciplina inicializarColecaoDisponibilidades(OfertaDisciplina oD){
        
        Session session = getSessionFactory().openSession();
        session.refresh(oD);
        Hibernate.initialize(oD);
        Hibernate.initialize(oD.getDisponibilidades());
        session.close();
        return oD;
        
    }
    
    //Filtro por disciplina e/ou turno e/ou campus
    public List<OfertaDisciplina> filtrarDTC(List<Disciplina> disciplinas, String turno, String campus) {

        try {
            List<OfertaDisciplina> turmas = new ArrayList<>();

            Session session = getSessionFactory().openSession();
            

            if(!disciplinas.isEmpty()){
                
                for (Disciplina d : disciplinas) {

                    Criteria criteria = session.createCriteria(OfertaDisciplina.class);
                    
                if (!campus.equals("")) {
                    criteria.add(Restrictions.eq("campus", campus));
                }

                if (!turno.equals("")) {
                    criteria.add(Restrictions.eq("turno", turno));
                }
                
//                    Query query = session.createQuery("from OfertaDisciplina t where t.disciplina_disciplina_id = :id ");
//                    query.setParameter("id", d.getID());
                criteria.add(Restrictions.eq("disciplina", d));
                List resultado = criteria.list();
                
//                    List resultado = query.list();
                    turmas.addAll(resultado);
            }
            }
            
            else{
                
                Criteria criteria = session.createCriteria(OfertaDisciplina.class);
                
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
    public List<OfertaDisciplina> filtrarDTCQ(List<Disciplina> disciplinas, String turno, String campus, int quadrimestre) {

        try {
            List<OfertaDisciplina> turmas = new ArrayList<>();

            Session session = getSessionFactory().openSession();

            if (!disciplinas.isEmpty()) {

                for (Disciplina d : disciplinas) {

                    Criteria criteria = session.createCriteria(OfertaDisciplina.class);

                    if (!campus.equals("")) {
                        criteria.add(Restrictions.eq("campus", campus));
                    }

                    if (!turno.equals("")) {
                        criteria.add(Restrictions.eq("turno", turno));
                    }

                    criteria.add(Restrictions.eq("disciplina", d));
                    criteria.add(Restrictions.eq("quadrimestre", quadrimestre));
                    List resultado = criteria.list();
                    turmas.addAll(resultado);
                }
            }
            
            else{
                
                Criteria criteria = session.createCriteria(OfertaDisciplina.class);
                
                if (!campus.equals("")) {
                    criteria.add(Restrictions.eq("campus", campus));
                }

                if (!turno.equals("")) {
                    criteria.add(Restrictions.eq("turno", turno));
                }
                
                if(quadrimestre != 0){
                    criteria.add(Restrictions.eq("quadrimestre", quadrimestre));
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
    
    /**
     * Lista as ofertas de disciplina por quadrimestre
     * @param quadrimestre int
     * @return Lista de ofertas de disciplinas filtradas
     */
    public List<OfertaDisciplina> findAllQuad(int quadrimestre) {
        
        Session session = getSessionFactory().openSession();
        Criteria crit = session.createCriteria(OfertaDisciplina.class);
        crit.add(Restrictions.eq("quadrimestre", quadrimestre));
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        List results = crit.list();
        
        session.close();
        return results;
    }

}


