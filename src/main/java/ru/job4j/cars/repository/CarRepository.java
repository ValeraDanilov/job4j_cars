package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class CarRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе
     *
     * @param car машина.
     * @return car с id.
     */
    public Car create(Car car) {
        this.crudRepository.run(session -> session.save(car));
        return car;
    }

    /**
     * Обновить в базе car.
     *
     * @param car машина.
     */
    public void update(Car car) {
        this.crudRepository.run(session -> session.merge(car));
    }

    /**
     * Удаление car по id.
     *
     * @param carId ID.
     */
    public void delete(int carId) {
        this.crudRepository.run("delete from Car where id = :fId",
                Map.of("fId", carId));
    }

    /**
     * Список машин отсортированных по id.
     *
     * @return список машин.
     */
    public List<Car> findAllOrderById() {
        return this.crudRepository.query("from Car car join fetch car.drivers", Car.class);
    }

    /**
     * Найти машину по ID
     *
     * @return машина.
     */
    public Optional<Car> findById(int carId) {
        return this.crudRepository.optional(
                "from Car car join fetch car.drivers where car.id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }
}
