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
        if (id <= all().size()) {
            return repository.getById(id).orElseThrow(NotFoundException::new);
        } else { // профилактика NotFoundException при существующих id
            return new Post(0, "Введено не корректное ID. Измените запрос, "
                    + "используйте диапазон id от 1 до " + all().size());
        }
    }

    public Post save(Post post) {
        if (post.getId() <= all().size()) {
            return repository.save(post);
        } else {
            return new Post(0, "Введено не корректное ID. Измените запрос, "
                    + "используйте диапазон id от 1 до " + all().size());
        }
    }

    public void removeById(long id) {
        repository.removeById(id);
    }
}