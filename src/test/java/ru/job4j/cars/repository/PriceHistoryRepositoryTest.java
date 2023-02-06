package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


public class PriceHistoryRepositoryTest {

    private static SessionFactory sessionFactory;

    private static PriceHistoryRepository priceHistoryRepository;

    @BeforeClass
    public static void initContext() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        priceHistoryRepository = new PriceHistoryRepository(new CrudRepository(sessionFactory));
    }

    @After
    public void tearDown() {
        var session = sessionFactory.openSession();
        var transaction = session.beginTransaction();
        session.createQuery("delete from PriceHistory ").executeUpdate();
        transaction.commit();
    }

    @Test
    public void create() {
        PriceHistory price = new PriceHistory(0, 200, 100, LocalDateTime.now());
        assertEquals(priceHistoryRepository.create(price), new PriceHistory(price.getId(), 200, 100, LocalDateTime.now()));
    }

    @Test
    public void update() {
        PriceHistory price = new PriceHistory(0, 200, 100, LocalDateTime.now());
        priceHistoryRepository.create(price);
        price.setBefore(300);
        priceHistoryRepository.update(price);
        assertEquals(priceHistoryRepository.findById(price.getId()).get().getBefore(), 300);
    }

    @Test
    public void delete() {
        PriceHistory price = new PriceHistory(0, 200, 100, LocalDateTime.now());
        priceHistoryRepository.create(price);
        priceHistoryRepository.delete(price.getId());
        assertNull(priceHistoryRepository.findById(price.getId()).orElse(null));
    }

    @Test
    public void findAllOrderById() {
        PriceHistory firstPriceHistory = new PriceHistory(0, 200, 100, LocalDateTime.now());
        PriceHistory secondPriceHistory = new PriceHistory(0, 300, 200, LocalDateTime.now());
        PriceHistory thirdPriceHistory = new PriceHistory(0, 400, 300, LocalDateTime.now());
        priceHistoryRepository.create(firstPriceHistory);
        priceHistoryRepository.create(secondPriceHistory);
        priceHistoryRepository.create(thirdPriceHistory);
        List<PriceHistory> res = List.of(
                new PriceHistory(firstPriceHistory.getId(), 200, 100, LocalDateTime.now()),
                new PriceHistory(secondPriceHistory.getId(), 300, 200, LocalDateTime.now()),
                new PriceHistory(thirdPriceHistory.getId(), 400, 300, LocalDateTime.now())
        );
        assertEquals(priceHistoryRepository.findAllOrderById(), res);
    }
}
