package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Post;

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
        this.crudRepository.run(session -> session.persist(post));
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
                "delete from Post where id = :fId",
                Map.of("fId", postId)
        );
    }

    /**
     * Список постов отсортированных по id.
     *
     * @return список постов.
     */
    public List<Post> findAllOrderById() {
        return this.crudRepository.query("from Post post join fetch post.participates join fetch post.history", Post.class);
    }

    /**
     * Найти объявления определенной марки.
     *
     * @param car car.
     * @return список постов.
     */
    public List<Post> findBySpecificBrand(Car car) {
        return this.crudRepository.query(
                "from Post post join fetch post.participates join fetch post.history"
                        + " where post.car.brand like :fKey", Post.class,
                Map.of("fKey", "%" + car.getBrand() + "%"));
    }

    /**
     * Найти объявления с фото.
     *
     * @param post post.
     * @return список постов.
     */
    public List<Post> findByPhoto(Post post) {
        return this.crudRepository.query("from Post post join fetch post.participates join fetch post.history "
                        + "where photo :fKey", Post.class,
                Map.of("fKey", Arrays.toString(post.getPhoto())));
    }

    /**
     * Найти объявления за последний день.
     *
     * @param post post.
     * @return список постов.
     */
    public List<Post> findByLastDay(Post post) {
        return crudRepository.query(
                "from Post post join fetch post.participates join fetch post.history where (extract(day from created))"
                        + " = (extract(day from current_date)) :fKey", Post.class,
                Map.of("fKey", post.getCreated()));
    }

    /**
     * Найти объявления о ID
     *
     * @return пост.
     */
    public Optional<Post> findById(int postId) {
        return this.crudRepository.optional(
                "from Post post join fetch post.participates where id = :fId", Post.class,
                Map.of("fId", postId)
        );
    }
}
