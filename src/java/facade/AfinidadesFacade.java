package facade;

import controller.HibernateUtil;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import model.Afinidades;
import model.Disciplina;
import model.Pessoa;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@Stateless
public class AfinidadesFacade extends AbstractFacade<Afinidades>{
    
    public AfinidadesFacade() {
        super(Afinidades.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
//    public List<Afinidades> findByName(String nome){
//        Session session = getSessionFactory().openSession();
//        Criteria criteria = session.createCriteria(Afinidades.class);
//        criteria.add(Restrictions.eq("nome", nome));
//        
//        List results = criteria.list();
//        session.close();
//        
//        return results;
//        
//    }
    
    
    public void salvarAfinidade(){
        
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        Disciplina d = (Disciplina)session.get(Disciplina.class, 2L);
        Pessoa p = (Pessoa)session.get(Pessoa.class, 2L);
        
        Afinidades afinidade = new Afinidades();
        
        afinidade.setDisciplina(d);
        afinidade.setPessoa(p);
        afinidade.setEstado("Adicionada");
        
        Calendar cal = Calendar.getInstance();
        afinidade.setDataAcao(cal.getTime());
        
        d.getAfinidades().add(afinidade);
        
        session.save(d);
        
        
        session.getTransaction().commit();
        session.close();
        
        
    }
    
    public Afinidades findByIDs(Long disciplinaId, Long pessoaId){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Afinidades.class);
            criteria.add(Restrictions.eq("disciplinaId", disciplinaId));
            criteria.add(Restrictions.eq("pessoaId", pessoaId));
            List resultado = criteria.list();
            

            if (resultado.size() <= 0) {
                session.close();
                return null;
            } else {
                Afinidades a = (Afinidades) resultado.get(0);
                session.close();
                return a;
            }
        } catch (HibernateException e) {
            return null;
        }
        
        
        
    }
    
  
    
    
    
}


