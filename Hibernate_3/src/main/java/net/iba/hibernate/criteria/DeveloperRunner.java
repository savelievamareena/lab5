package net.iba.hibernate.criteria;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DeveloperRunner {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(net.iba.hibernate.criteria.Developer.class);

        DeveloperRunner developerRunner = new DeveloperRunner();

        System.out.println("Adding developer's records to the database...");
        Integer developerId1 = developerRunner.addDeveloper("Ihor", "Developer", "Java Developer", 3, 2000);
        Integer developerId2 = developerRunner.addDeveloper("First", "Developer", "C++ Developer", 10, 2000);
        Integer developerId3 = developerRunner.addDeveloper("Second", "Developer", "C# Developer", 5, 2000);
        Integer developerId4 = developerRunner.addDeveloper("Third", "Developer", "PHP Developer", 1, 2000);

        System.out.println("List of Developers with experience more than 3 years:");
        developerRunner.listDevelopersOverThreeYears();

        System.out.println("Total Salary of all Developers:");
        developerRunner.totalSalary();
        sessionFactory.close();
    }

    public Integer addDeveloper(String firstName, String lastName, String specialty, int experience, int salary) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Developer developer = new Developer(firstName, lastName, specialty, experience, salary);
        Integer developerId = (Integer) session.save(developer);
        transaction.commit();
        session.close();
        return developerId;
    }

    public void listDevelopersOverThreeYears() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Developer.class);
        criteria.add(Restrictions.gt("experience", 3));
        List<Developer> developers = criteria.list();

        for (Developer developer : developers) {
            System.out.println("=======================");
            System.out.println(developer);
            System.out.println("=======================");
        }
        transaction.commit();
        session.close();
    }

    public void totalSalary() {
        Session session  = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Criteria criteria = session.createCriteria(Developer.class);
        criteria.setProjection(Projections.sum("salary"));

        List totalSalary = criteria.list();
        System.out.println("Total salary of all developers: " + totalSalary.get(0));
        transaction.commit();
        session.close();
    }
}
