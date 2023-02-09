package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.PriceHistory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PriceHistoryRepository {

    private final CrudRepository crudRepository;


    public PriceHistory create(PriceHistory priceHistory) {
        this.crudRepository.run(session -> session.save(priceHistory));
        return priceHistory;
    }

    public Optional<PriceHistory> findById(int priceHistoryId) {
        return this.crudRepository.optional("from PriceHistory where id = :Id", PriceHistory.class,
                Map.of("Id", priceHistoryId));
    }

    public void update(PriceHistory priceHistory) {
        this.crudRepository.run(session -> session.merge(priceHistory));
    }

    public void delete(int priceHistoryId) {
        this.crudRepository.run("delete from PriceHistory where id = :Id", Map.of("Id", priceHistoryId));
    }

    public List<PriceHistory> findAllOrderById() {
        return this.crudRepository.query("from PriceHistory", PriceHistory.class);
    }
}
