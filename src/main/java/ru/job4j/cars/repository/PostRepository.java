package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class PostRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param post пост.
     * @return пост с id.
     */
    public Post create(Post post) {
        this.crudRepository.run(session -> session.save(post));
        return post;
    }

    /**
     * Обновить в базе пост.
     *
     * @param post пост.
     */
    public void update(Post post) {
        this.crudRepository.run(session -> session.merge(post));
    }

    /**
     * Удалить пост по id.
     *
     * @param postId ID
     */
    public void delete(int postId) {
        this.crudRepository.run(
                "delete from Post p where p.id = :fId",
                Map.of("fId", postId)
        );
    }

    /**
     * Список постов отсортированных по id.
     *
     * @return список постов.
     */
    public List<Post> findAllOrderById() {
        return this.crudRepository.query("from Post post"
                + " left join fetch post.history"
                + " left join fetch post.participates", Post.class);
    }

    /**
     * Найти объявления определенной марки.
     *
     * @param car car.
     * @return список постов.
     */
    public List<Post> findBySpecificBrand(Car car) {
        return this.crudRepository.query(
                "from Post post"
                        + " left join fetch post.history"
                        + " left join fetch post.participates"
                        + " where post.car.brand like :fKey", Post.class,
                Map.of("fKey", "%" + car.getBrand() + "%"));
    }

    /**
     * Найти объявления с фото.
     *
     * @return список постов.
     */
    public List<Post> findByPhoto() {
        return this.crudRepository.query("from Post where photo is not null", Post.class);
    }

    /**
     * Найти объявления за последний день.
     *
     * @return список постов.
     */
    public List<Post> findByLastDay() {
        return crudRepository.query(
                "from Post where created > :fTime",
                Post.class,
                Map.of("fTime", LocalDateTime.now().minusDays(1)));
    }

    /**
     * Найти объявления о ID
     *
     * @return пост.
     */
    public Optional<Post> findById(int postId) {
        return this.crudRepository.optional(
                "from Post post join fetch post.participates where post.id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }
}
