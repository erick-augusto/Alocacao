package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.Docente;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Stateless
public class DocenteFacade extends AbstractFacade<Docente>{
    
    public DocenteFacade() {
        super(Docente.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
    //Busca docentes pelo nome
    public List<Docente> findByName(String nome){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Docente.class);
        criteria.add(Restrictions.eq("nome", nome));
        
        List results = criteria.list();
        session.close();
        
        return results;        
    }
    
//    
//    
//    public Pessoa findByLogin(String login) {
//
////        String email = login + "@aluno.ufabc.edu.br";
////        email.substring(0, email.lastIndexOf("@"))
//        try {
////                Session session = getSessionFactory().openSession();
////                Query query = session.createQuery("from Pessoa p where p.email = :email ");
////                query.setParameter("email", email);
////                List resultado = query.list();
//
//            login = login + "%";
//            Session session = getSessionFactory().openSession();
//            Criteria criteria = session.createCriteria(Pessoa.class);
//            criteria.add(Restrictions.like("email", login));
//            List resultado = criteria.list();
//
//            if (resultado.size() <= 0) {
//                session.close();
//                return null;
//            } else {
//                Pessoa userFound = (Pessoa) resultado.get(0);
//                session.close();
//                return userFound;
//            }
//        } catch (HibernateException e) {
//            return null;
//        }
//
//    }
//    
//    public Pessoa findByLogin2(String email) {
//
//
//            try {
//                email = email + "%";
//                Session session = getSessionFactory().openSession();
//                Query query = session.createQuery("from Pessoa p where p.email like :email ");
//                query.setParameter("email", email);
//                List resultado = query.list();
//
//                if (resultado.size() == 1) {
//                    Pessoa userFound = (Pessoa) resultado.get(0);
//                    return userFound;
//                } else {
//                    return null;
//                }
//            } catch (HibernateException e) {
//                return null;
//            }
//
//    }

}
