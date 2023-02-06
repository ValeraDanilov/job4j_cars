package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class PostRepositoryTest {

    private static SessionFactory sessionFactory;
    private static PostRepository postRepository;
    private static CarRepository carRepository;
    private static EngineRepository engineRepository;
    private static UserRepository userRepository;
    private static PriceHistoryRepository priceHistoryRepository;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        postRepository = new PostRepository(new CrudRepository(sessionFactory));
        carRepository = new CarRepository(new CrudRepository(sessionFactory));
        engineRepository = new EngineRepository(new CrudRepository(sessionFactory));
        userRepository = new UserRepository(new CrudRepository(sessionFactory));
        priceHistoryRepository = new PriceHistoryRepository(new CrudRepository(sessionFactory));
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from Post ").executeUpdate();
        session.createQuery("delete from Car ").executeUpdate();
        session.createQuery("delete from Engine ").executeUpdate();
        session.createQuery("delete from User ").executeUpdate();
        session.createQuery("delete from PriceHistory ").executeUpdate();
        transaction.commit();
    }


    @Test
    public void create() {
        Engine engine = new Engine(0, "test");
        engineRepository.create(engine);
        Car car = new Car(0, "test", "test", "test", engine, null, "test", "test", "test");
        carRepository.create(car);
        Post post = new Post(0, "test", LocalDateTime.now(), null, null, car, null, null);
        assertEquals(postRepository.create(post), new Post(post.getId(), "test", LocalDateTime.now(), null, null, car, null, null));
    }

    @Test
    public void update() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Engine engine = new Engine(0, "test");
        engineRepository.create(engine);
        Car car = new Car(0, "test", "test", "test", engine, null, "test", "test", "test");
        carRepository.create(car);
        Post post = new Post(0, "test", LocalDateTime.now(), null, user, car, null, Set.of(user));
        postRepository.create(post);
        post.setText("newTest");
        postRepository.update(post);
        assertEquals(postRepository.findById(post.getId()).get().getText(), "newTest");
    }

    @Test
    public void delete() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Engine engine = new Engine(0, "test");
        engineRepository.create(engine);
        Car car = new Car(0, "test", "test", "test", engine, null, "test", "test", "test");
        carRepository.create(car);
        Post post = new Post(0, "test", LocalDateTime.now(), null, user, car, null, Set.of(user));
        postRepository.create(post);
        postRepository.delete(post.getId());
        assertNull(postRepository.findById(post.getId()).orElse(null));
    }

    @Test
    public void findAllOrderById() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        Engine engine = new Engine(0, "test");
        engineRepository.create(engine);
        Car car = new Car(0, "test", "test", "test", engine, null, "test", "test", "test");
        carRepository.create(car);
        Post firstPost = new Post(0, "test1", LocalDateTime.now(), null, user, car, null, Set.of(user));
        Post secondPost = new Post(0, "test2", LocalDateTime.now(), null, user, car, null, Set.of(user));
        Post thirdPost = new Post(0, "test3", LocalDateTime.now(), null, user, car, null, Set.of(user));
        postRepository.create(firstPost);
        postRepository.create(secondPost);
        postRepository.create(thirdPost);
        List<Post> res = List.of(
                new Post(firstPost.getId(), "test1", LocalDateTime.now(), null, user, car, null, Set.of(user)),
                new Post(secondPost.getId(), "test2", LocalDateTime.now(), null, user, car, null, Set.of(user)),
                new Post(thirdPost.getId(), "test3", LocalDateTime.now(), null, user, car, null, Set.of(user))
        );
        assertEquals(postRepository.findAllOrderById().size(), res);
    }

    @Test
    public void findBySpecificBrand() {
    }

    @Test
    public void findByPhoto() {
    }

    @Test
    public void findByLastDay() {
    }
}
