package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static org.junit.Assert.*;

public class EngineRepositoryTest {

    private static SessionFactory sessionFactory;
    private static EngineRepository engineRepository;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        engineRepository = new EngineRepository(new CrudRepository(sessionFactory));
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from Engine ").executeUpdate();
        transaction.commit();
    }

    @Test
    public void create() {
        Engine engine = new Engine();
        engine.setName("test");
        assertEquals(engineRepository.create(engine), new Engine(engine.getId(), "test"));
    }

    @Test
    public void update() {
        Engine engine = new Engine();
        engine.setName("test");
        engineRepository.create(engine);
        engine.setName("newTest");
        Engine newEngine = new Engine(engine.getId(), "newTest");
        engineRepository.update(engine);
        assertEquals(engineRepository.findById(engine.getId()).get(), newEngine);
    }

    @Test
    public void delete() {
        Engine engine = new Engine();
        engine.setName("test");
        engineRepository.create(engine);
        engineRepository.delete(engine.getId());
        assertNull(engineRepository.findById(engine.getId()).orElse(null));
    }

    @Test
    public void findAllOrderById() {
        Engine firstEngine = new Engine(0, "Test");
        Engine secondEngine = new Engine(0, "Test1");
        Engine thirdEngine = new Engine(0, "Test2");
        engineRepository.create(firstEngine);
        engineRepository.create(secondEngine);
        engineRepository.create(thirdEngine);
        List<Engine> res = List.of(
                new Engine(firstEngine.getId(), "Test"),
                new Engine(secondEngine.getId(), "Test1"),
                new Engine(thirdEngine.getId(), "Test2")
        );
        assertEquals(engineRepository.findAllOrderById(), res);
    }
}
