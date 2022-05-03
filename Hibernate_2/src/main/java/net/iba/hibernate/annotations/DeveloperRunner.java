package net.iba.hibernate.annotations;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DeveloperRunner {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(net.iba.hibernate.annotations.Developer.class);

        DeveloperRunner developerRunner = new DeveloperRunner();

        System.out.println("Adding developer's records to the DB");

        developerRunner.addDeveloper("Ihor", "Developer", "Java Developer", 2);
        developerRunner.addDeveloper("Some", "Developer", "C++ Developer", 2);
        developerRunner.addDeveloper("Peter", "UI", "UI Developer", 4);

        System.out.println("List of developers");

        List<Developer> developers = developerRunner.listDevelopers();
        for (Developer developer : developers) {
            System.out.println(developer);
        }
        System.out.println("===================================");
        System.out.println("Removing Some Developer and updating Ihor");

        developerRunner.updateDeveloper(21, 3);
        developerRunner.removeDeveloper(13);

        System.out.println("Final list of developers");

        developers = developerRunner.listDevelopers();
        for (Developer developer : developers) {
            System.out.println(developer);
        }
        System.out.println("===================================");
    }

    public void addDeveloper(String firstName, String lastName, String specialty, int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Developer developer = new Developer(firstName, lastName, specialty, experience);
        session.save(developer);
        transaction.commit();
        session.close();
    }

    public List<Developer> listDevelopers() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<Developer> developers = session.createQuery("FROM Developer").list();

        transaction.commit();
        session.close();
        return developers;
    }

    public void updateDeveloper(int developerId, int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = (Developer) session.get(Developer.class, developerId);
        developer.setExperience(experience);
        session.update(developer);
        transaction.commit();
        session.close();
    }

    public void removeDeveloper(int developerId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = (Developer) session.get(Developer.class, developerId);
        session.delete(developer);
        transaction.commit();
        session.close();
    }
}
