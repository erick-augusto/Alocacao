package facade;

import controller.HibernateUtil;
import java.util.List;
import javax.ejb.Stateless;
import model.Pessoa;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@Stateless
public class PessoaFacade extends AbstractFacade<Pessoa>{
    
    public PessoaFacade() {
        super(Pessoa.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
//    public Pessoa reinicializarUsuario(Pessoa p) {
//
//        Session session = getSessionFactory().openSession();
//        session.refresh(p);
//        return p;
//
//    }
    
    public List<Pessoa> findByName(String nome){
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Pessoa.class);
        criteria.add(Restrictions.eq("nome", nome));
        
        List results = criteria.list();
        session.close();
        
        return results;
        
    }
    
    
    
    public Pessoa findByLogin(String login) {

//        String email = login + "@aluno.ufabc.edu.br";
//        email.substring(0, email.lastIndexOf("@"))
        try {
//                Session session = getSessionFactory().openSession();
//                Query query = session.createQuery("from Pessoa p where p.email = :email ");
//                query.setParameter("email", email);
//                List resultado = query.list();

            login = login + "%";
            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Pessoa.class);
            criteria.add(Restrictions.like("email", login));
            List resultado = criteria.list();

            if (resultado.size() <= 0) {
                session.close();
                return null;
            } else {
                Pessoa userFound = (Pessoa) resultado.get(0);
                session.close();
                return userFound;
            }
        } catch (HibernateException e) {
            return null;
        }

    }
    
    public Pessoa findByLogin2(String email) {


            try {
                email = email + "%";
                Session session = getSessionFactory().openSession();
                Query query = session.createQuery("from Pessoa p where p.email like :email ");
                query.setParameter("email", email);
                List resultado = query.list();

                if (resultado.size() == 1) {
                    Pessoa userFound = (Pessoa) resultado.get(0);
                    return userFound;
                } else {
                    return null;
                }
            } catch (HibernateException e) {
                return null;
            }

    }
    
    //Retorna usuários administradores do sistema
    public List<Pessoa> listAdms() {

        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Pessoa.class);
        criteria.add(Restrictions.eq("adm", true));

        List results = criteria.list();
        session.close();

        return results;

    }
    
    public List<Pessoa> listDocentes(){
        
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(Pessoa.class);
        criteria.add(Restrictions.eq("class", "Docente"));

        List results = criteria.list();
        session.close();

        return results;
        
        
    }

}
