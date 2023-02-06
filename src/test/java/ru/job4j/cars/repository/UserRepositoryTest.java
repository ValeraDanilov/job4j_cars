package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.List;

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
        session.createQuery("delete from User ").executeUpdate();
        transaction.commit();
    }

    @Test
    public void create() {
        User user = new User();
        user.setLogin("Ira");
        user.setPassword("1");
        userRepository.create(user);
        var res = userRepository.findById(user.getId());
        User checkUser = new User();
        checkUser.setLogin("Ira");
        checkUser.setPassword("1");
        checkUser.setId(res.get().getId());
        assertEquals(res.get(), checkUser);
    }

    @Test
    public void update() {
        User user = new User(0, "Ira1", "1");
        userRepository.create(user);
        user.setLogin("Ira");
        userRepository.update(user);
        var res = userRepository.findById(user.getId());
        User checkUser = new User(res.get().getId(), "Ira", "1");
        assertEquals(res.get(), checkUser);
    }

    @Test
    public void delete() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        userRepository.delete(user.getId());
        var res = userRepository.findById(user.getId()).orElse(null);
        assertNull(res);
    }

    @Test
    public void findAllOrderById() {
        User firstUser = new User(0, "Ira", "1");
        User secondUser = new User(0, "Valera", "2");
        User thirdUser = new User(0, "Dima", "1");
        userRepository.create(firstUser);
        userRepository.create(secondUser);
        userRepository.create(thirdUser);
        List<User> res = List.of(
                new User(firstUser.getId(), "Ira", "1"),
                new User(secondUser.getId(), "Valera", "2"),
                new User(thirdUser.getId(), "Dima", "1")
        );
        assertEquals(userRepository.findAllOrderById(), res);
    }

    @Test
    public void findByLikeLogin() {
        User firstUser = new User(0, "Ira", "1");
        User secondUser = new User(0, "Valera", "2");
        User thirdUser = new User(0, "Nik", "1");
        userRepository.create(firstUser);
        userRepository.create(secondUser);
        userRepository.create(thirdUser);
        List<User> res = List.of(
                new User(firstUser.getId(), "Ira", "1"),
                new User(secondUser.getId(), "Valera", "2")
        );
        assertEquals(userRepository.findByLikeLogin("a"), res);
    }

    @Test
    public void findByLogin() {
        User user = new User(0, "Ira", "1");
        userRepository.create(user);
        assertEquals(userRepository.findByLogin("Ira").get(), new User(user.getId(), "Ira", "1"));
    }
}
