package ru.netology.repository;

import ru.netology.model.Post;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
    private final Map<Long, Post> allPosts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    public Map<Long, Post> all() { //мапа показалась удобней, быстрее и безопасней списка
        return allPosts;
    }

    public Optional<Post> getById(long id) {
        return Optional.of(allPosts.get(id));
    }

    public Post save(Post post) {
        long idInList = counter.getAndIncrement();

        if (post.getId() == 0) {
            post.setId(idInList + 1);
            allPosts.put(idInList + 1, post);
        } else {
            allPosts.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        allPosts.remove(id);
    }
}
