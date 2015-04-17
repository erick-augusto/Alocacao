package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import model.Afinidade;
import model.Disciplina;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@Stateless
public class DisciplinaFacade extends AbstractFacade<Disciplina> {

    public DisciplinaFacade() {
        super(Disciplina.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }

    public List<Disciplina> findByName(String nome) {
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Disciplina.class);
        criteria.add(Restrictions.eq("nome", nome));

        List results = criteria.list();
        session.close();

        return results;

    }
    
    //Localiza uma disciplina pelo código
    public Disciplina findByCod(String codigo){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Disciplina.class);
        criteria.add(Restrictions.eq("codigo", codigo));
        
        List<Disciplina> results = criteria.list();
        session.close();
        
        return results.get(0);
    }
    
    public Disciplina findByCodOrName(String codigo, String nome){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Disciplina.class);
//        criteria.add(Restrictions.eq("codigo", codigo));
        criteria.add(Restrictions.or(Restrictions.eq("codigo", codigo), Restrictions.eq("nome", nome)));
        
        List<Disciplina> results = criteria.list();
        session.close();
        
        if(!results.isEmpty()){
            return results.get(0);
        }
        
        else{
            return null;
        }
        
        
    }

    public Disciplina inicializarColecaoAfinidades(Disciplina d) {

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

            if (!eixos.isEmpty()) {

                for (String eixo : eixos) {
                    Query query = session.createQuery("from Disciplina d where d.eixo = :eixo ");
                    query.setParameter("eixo", eixo);
                    List resultado = query.list();
                    disciplinas.addAll(resultado);
                }

            }

            if (!cursos.isEmpty()) {

                for (String curso : cursos) {
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

//    @Override
//    public void remove(Disciplina d) {
//        Session session = getSessionFactory().openSession();
//
//        Transaction transaction = session.beginTransaction();
//
////        session.refresh(d);
////        Hibernate.initialize(d);
////        Hibernate.initialize(d.getAfinidades());
////
////        Set<Afinidades> afs = d.getAfinidades();
////
////        AfinidadesFacade afinidadeFacade = new AfinidadesFacade();
////
////        for (Afinidade a : afs) {
////
////            afinidadeFacade.remove(a);
////        }
//
//        session.delete(d);
//        try {
//            transaction.commit();
//        } finally {
//            session.close();
//        }
//    }

}

