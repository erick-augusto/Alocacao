
package controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    
    private static final SessionFactory sessionFactory;
    
    static {
//        try {
//                Configuration cfg = new Configuration().configure("hibernate.cfg.xml");         
//                StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
//                sb.applySettings(cfg.getProperties());
//                StandardServiceRegistry standardServiceRegistry = sb.build();                   
//                sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);              
//            } catch (Throwable th) {
//                    System.err.println("Initial SessionFactory creation failed" + th);
//                    throw new ExceptionInInitializerError(th);
//            }
        
        // logger.info("Trying to create a test connection with the database.");
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
             sessionFactory = configuration.buildSessionFactory(ssrb.build());
            Session session = sessionFactory.openSession();
            //logger.info("Test connection with the database created successfuly.");
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
}
