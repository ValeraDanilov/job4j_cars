package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Driver;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CarRepositoryTest {

    private static SessionFactory sessionFactory;
    private static CarRepository carRepository;
    private static EngineRepository engineRepository;
    private static DriverRepository driverRepository;
    private static UserRepository userRepository;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        carRepository = new CarRepository(new CrudRepository(sessionFactory));
        engineRepository = new EngineRepository(new CrudRepository(sessionFactory));
        driverRepository = new DriverRepository(new CrudRepository(sessionFactory));
        userRepository = new UserRepository(new CrudRepository(sessionFactory));
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from Car ").executeUpdate();
        session.createQuery("delete from Engine ").executeUpdate();
        session.createQuery("delete from Driver ").executeUpdate();
        session.createQuery("delete from User ").executeUpdate();
        transaction.commit();
    }

    @Test
    public void create() {
        Engine engine = new Engine(0, "Test");
        engineRepository.create(engine);
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        driverRepository.create(driver);
        Car car = new Car(0, "test", "test", "test", engine, Set.of(driver), "test", "test", "test");
        assertEquals(carRepository.create(car), new Car(car.getId(), "test", "test", "test", engine, null, "test", "test", "test"));
    }

    @Test
    public void update() {
        Engine engine = new Engine(0, "Test");
        engineRepository.create(engine);
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        driverRepository.create(driver);
        Car car = new Car(0, "test", "test", "test", engine, Set.of(driver), "test", "test", "test");
        carRepository.create(car);
        car.setBrand("Audi");
        carRepository.update(car);
        assertEquals(carRepository.findById(car.getId()).get().getBrand(), "Audi");
    }

    @Test
    public void delete() {
        Engine engine = new Engine(0, "Test");
        engineRepository.create(engine);
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        driverRepository.create(driver);
        Car car = new Car(0, "test", "test", "test", engine, Set.of(driver), "test", "test", "test");
        carRepository.create(car);
        carRepository.delete(car.getId());
        assertNull(carRepository.findById(car.getId()).orElse(null));
    }

    @Test
    public void findAllOrderById() {
        Engine engine = new Engine(0, "Test");
        engineRepository.create(engine);
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Driver driver = new Driver(0, "Test", user);
        driverRepository.create(driver);
        Car firstCar = new Car(0, "Audi", "test", "test", engine, Set.of(driver), "test", "test", "test");
        Car secondCar = new Car(0, "BMW", "test", "test", engine, Set.of(driver), "test", "test", "test");
        Car thirdCar = new Car(0, "Mercedes", "test", "test", engine, Set.of(driver), "test", "test", "test");
        carRepository.create(firstCar);
        carRepository.create(secondCar);
        carRepository.create(thirdCar);
        List<Car> res = List.of(
                new Car(firstCar.getId(), "Audi", "test", "test", engine, Set.of(driver), "test", "test", "test"),
                new Car(secondCar.getId(), "BMW", "test", "test", engine, Set.of(driver), "test", "test", "test"),
                new Car(thirdCar.getId(), "Mercedes", "test", "test", engine, Set.of(driver), "test", "test", "test")
        );
        assertEquals(carRepository.findAllOrderById(), res);
    }
}
