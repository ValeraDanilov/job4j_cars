package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.Driver;
import ru.job4j.cars.model.User;

import java.util.List;

import static org.junit.Assert.*;

public class DriverRepositoryTest {

    private static SessionFactory sessionFactory;

    private static DriverRepository driverRepository;
    private static UserRepository userRepository;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        driverRepository = new DriverRepository(new CrudRepository(sessionFactory));
        userRepository = new UserRepository(new CrudRepository(sessionFactory));
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from Driver ").executeUpdate();
        session.createQuery("delete from User ").executeUpdate();
        transaction.commit();
    }


    @Test
    public void create() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        assertEquals(driverRepository.create(driver), new Driver(driver.getId(), "Test", user));
    }

    @Test
    public void update() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        driverRepository.create(driver);
        driver.setName("newTest");
        driverRepository.update(driver);
        assertEquals(driverRepository.findById(driver.getId()).get(), new Driver(driver.getId(), "newTest", user));
    }

    @Test
    public void delete() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        driverRepository.create(driver);
        driverRepository.delete(driver.getId());
        assertNull(driverRepository.findById(driver.getId()).orElse(null));
    }

    @Test
    public void findAllOrderById() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver firstDriver = new Driver(0, "Test", user);
        Driver secondDriver = new Driver(0, "Test1", user);
        Driver thirdDriver = new Driver(0, "Test2", user);
        driverRepository.create(firstDriver);
        driverRepository.create(secondDriver);
        driverRepository.create(thirdDriver);
        List<Driver> res = List.of(
                new Driver(firstDriver.getId(), "Test", user),
                new Driver(secondDriver.getId(), "Test1", user),
                new Driver(thirdDriver.getId(), "Test2", user)
        );
        assertEquals(driverRepository.findAllOrderById(), res);
    }
}
