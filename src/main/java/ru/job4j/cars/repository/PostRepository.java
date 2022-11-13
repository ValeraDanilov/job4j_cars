package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

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
        return this.crudRepository.query("from Post post join fetch post.participates", Post.class);
    }

    /**
     * Найти пост по ID
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
