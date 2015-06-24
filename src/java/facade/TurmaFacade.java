/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import controller.HibernateUtil;
import model.Turma;
import org.hibernate.SessionFactory;

/**
 *
 * @author Juliana
 */
public class TurmaFacade extends AbstractFacade<Turma>{
    
    public TurmaFacade() {
        super(Turma.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
}