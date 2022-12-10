package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserRepositoryTest {

    private static SessionFactory sessionFactory;

    private static UserRepository userRepository;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        userRepository = new UserRepository(new CrudRepository(sessionFactory));
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
       // session.createQuery("delete from User ").executeUpdate();
        transaction.commit();
    }

    @Test
    public void create() {
        User user = new User();
        user.setLogin("Ira");
        user.setPassword("1");
        userRepository.create(user);

       // assertEquals("Ira", findByUser.get().getLogin());
    }
}
