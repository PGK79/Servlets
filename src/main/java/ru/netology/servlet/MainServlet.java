package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final PostRepository REPOSITORY = new PostRepository();
    private static final PostService SERVICE = new PostService(REPOSITORY);
    private static final String REQUEST_PATH = "/api/posts";

    @Override
    public void init() {
        controller = new PostController(SERVICE);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            final boolean pathWithId = path.matches("/api/posts/\\d+");
            final var id = receiveId(path, pathWithId);

            if (method.equals("GET") && path.equals(REQUEST_PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals("GET") && pathWithId) {
                controller.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals(REQUEST_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && pathWithId) {
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public long receiveId(String path, boolean pathWithId) {
        if (pathWithId) {
            return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        } else return 0;
    }
}