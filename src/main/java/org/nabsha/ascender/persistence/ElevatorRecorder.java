package org.nabsha.ascender.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.nabsha.ascender.domain.elevator.ElevatorModel;

import java.util.List;

/**
 * Created by Nabeel Shaheen on 4/08/2015.
 */
public class ElevatorRecorder {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        try {
            return new AnnotationConfiguration()
                    .configure()
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void save(ElevatorModel model) {

        Session session = sessionFactory.openSession();
        session.save(model);
        session.flush();
        session.close();
    }

    public static List<ElevatorModel> getAll() {

        List<ElevatorModel> list = sessionFactory.openSession().createCriteria(ElevatorModel.class).list();
        sessionFactory.close();
        return list;
    }

}
