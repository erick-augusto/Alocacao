package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
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
    
    public List<Disciplina> findByEixoCurso(List<String> eixos, List<String> cursos) {

        
            List<Disciplina> disciplinas = new ArrayList<>();

            try {
                
                Session session = getSessionFactory().openSession();
                
                if(!eixos.isEmpty()){
                    
                    for(String eixo: eixos){
                    Query query = session.createQuery("from Disciplina d where d.eixo = :eixo ");
                    query.setParameter("eixo", eixo);
                    List resultado = query.list();
                    disciplinas.addAll(resultado);
                }
                    
                }
                
                if(!cursos.isEmpty()){
                    
                    for(String curso: cursos){
                    Query query = session.createQuery("from Disciplina d where d.curso = :curso ");
                    query.setParameter("curso", curso);
                    List resultado = query.list();
                    disciplinas.addAll(resultado);
                    }
                    
                }
                
                
                
                return disciplinas;
                
            } catch (HibernateException e) {
                return null;
            }

    }
    
    
}


