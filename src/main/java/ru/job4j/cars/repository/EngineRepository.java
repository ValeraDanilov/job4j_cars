package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class EngineRepository {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param engine двигатель.
     */
    public Engine create(Engine engine) {
        this.crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    /**
     * Обновить в базе двигателей.
     *
     * @param engine пользователь.
     */
    public void update(Engine engine) {
        this.crudRepository.run(session -> session.merge(engine));
    }

    /**
     * Удалить двигатель по id.
     *
     * @param engineId ID
     */
    public void delete(int engineId) {
        this.crudRepository.run(
                "delete from Engine where id = :fId",
                Map.of("fId", engineId)
        );
    }

    /**
     * Список двигателей отсортированных по id.
     *
     * @return список двигателей.
     */
    public List<Engine> findAllOrderById() {
        return this.crudRepository.query("from Engine", Engine.class);
    }

    /**
     * Найти двигатель по ID
     *
     * @return двигатель.
     */
    public Optional<Engine> findById(int engineId) {
        return this.crudRepository.optional(
                "from Engine where id = :fId", Engine.class,
                Map.of("fId", engineId)
        );
    }
}
