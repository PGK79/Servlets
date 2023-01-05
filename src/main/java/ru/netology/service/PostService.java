package ru.netology.service;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.repository.PostRepository;

import java.util.Map;

public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Map<Long, Post> all() {
        return repository.all();
    }

    public Post getById(long id) {
        if (id <= all().size() && all().containsKey(id)) {
            return repository.getById(id).orElseThrow(NotFoundException::new);
        } else {
            return new Post(0, "Сообщения с таким ID не существует");
        }
    }

    public Post save(Post post) {
        if (post.getId() <= all().size()) {
            return repository.save(post);
        } else {
            return new Post(0, "Введено не корректное ID. Измените ID");
        }
    }

    public String removeById(long id) {
        if (all().containsKey(id)) {
            repository.removeById(id);
            return "Пост удален";
        } else {
            return "Пост не найден";
        }
    }
}