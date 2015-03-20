package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@Stateless
public class DisponibilidadeFacade extends AbstractFacade<Disponibilidade>{
    
    public DisponibilidadeFacade() {
        super(Disponibilidade.class);
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
    
    
//    public void salvarAfinidade(){
//        
//        Session session = getSessionFactory().openSession();
//        session.beginTransaction();
//        Disciplina d = (Disciplina)session.get(Disciplina.class, 2L);
//        Pessoa p = (Pessoa)session.get(Pessoa.class, 2L);
//        
//        Afinidades afinidade = new Afinidades();
//        
//        afinidade.setDisciplina(d);
//        afinidade.setPessoa(p);
//        afinidade.setEstado("Adicionada");
//        
//        Calendar cal = Calendar.getInstance();
//        afinidade.setDataAcao(cal.getTime());
//        
//        d.getAfinidades().add(afinidade);
//        
//        session.save(d);
//        
//        
//        session.getTransaction().commit();
//        session.close();
//        
//        
//    }
    
    public Disponibilidade findByIDs(Long turmaId, Long pessoaId){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("turmaId", turmaId));
            criteria.add(Restrictions.eq("pessoaId", pessoaId));
            List resultado = criteria.list();
            

            if (resultado.size() <= 0) {
                session.close();
                return null;
            } else {
                Disponibilidade d = (Disponibilidade) resultado.get(0);
                session.close();
                return d;
            }
        } catch (HibernateException e) {
            return null;
        }
        
        
        
    }
    
    public List<Disponibilidade> findByPessoa(Pessoa pessoa){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("pessoa", pessoa));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List resultado = criteria.list();
            
            session.close();
            return resultado;
            
        } catch (HibernateException e) {
            return null;
        }
     
    }
    
    //Retorna a lista de disponibilidade de acordo com a disciplina pesquisada
    //Turno e campus são atributos opcionais
    public List<Disponibilidade> findByDisciplinaTC(Disciplina d, String campus, String turno){
        
        try{
            
            Session session = getSessionFactory().openSession();
            Criteria crit = session.createCriteria(Disponibilidade.class);
            
            if(d != null){
                
                crit.createAlias("turma", "t").add(Restrictions.eq("t.disciplina", d));
                
                if(!campus.equals("")){
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                
                if(!turno.equals("")){
                    crit.add(Restrictions.eq("t.turno", turno));
                }
                
                
            }
            else{
                
                crit.createAlias("turma", "t");
                
                if(!campus.equals("")){
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                
                if(!turno.equals("")){
                    crit.add(Restrictions.eq("t.turno", turno));
                }
            }
            
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List result = crit.list();
            return result;
            
        }
        catch(HibernateException e){
            return null;
        }

        
    }
    
  
    
    
    
}
