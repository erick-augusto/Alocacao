package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import model.Docente;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
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
    
    public List<Docente> findByCentroArea(List<String> centros, List<String> areas) {

        List<Docente> docentes = new ArrayList<>();

        try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Docente.class);
            

            if (!centros.isEmpty()) {

                for (String centro : centros) {
                    criteria.add(Restrictions.eq("centro", centro));
                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    List resultado = criteria.list();
                    docentes.addAll(resultado);
                }

            }

            if (!areas.isEmpty()) {

                for (String area : areas) {
                    criteria.add(Restrictions.eq("areaAtuacao", area));
                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    List resultado = criteria.list();
                    docentes.addAll(resultado);
                }

            }

            return docentes;

        } catch (HibernateException e) {
            return null;
        }

    }


}
