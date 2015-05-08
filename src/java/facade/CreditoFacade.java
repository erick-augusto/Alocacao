package facade;

import controller.HibernateUtil;
import javax.ejb.Stateless;
import model.Credito;
import org.hibernate.SessionFactory;


@Stateless
public class CreditoFacade extends AbstractFacade<Credito>{
    
    public CreditoFacade() {
        super(Credito.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }

    
}


