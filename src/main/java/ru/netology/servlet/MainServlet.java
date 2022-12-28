package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    @Override
    public void init() {
        final var REPOSITORY = new PostRepository();
        final var SERVICE = new PostService(REPOSITORY);
        controller = new PostController(SERVICE);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var PATH = req.getRequestURI();
            final var METHOD = req.getMethod();
            final var REQUEST_PATH = "/api/posts";
            final var REQUEST_PATH_ID = "/api/posts/\\d+";
            final var ID = receiveId(PATH,REQUEST_PATH_ID);

            // primitive routing
            if (METHOD.equals("GET") && PATH.equals(REQUEST_PATH)) {
                controller.all(resp);
                return;
            }
            if (METHOD.equals("GET") && PATH.matches(REQUEST_PATH_ID)) {
                // easy way
                controller.getById(ID, resp);
                return;
            }
            if (METHOD.equals("POST") && PATH.equals(REQUEST_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (METHOD.equals("DELETE") && PATH.matches(REQUEST_PATH_ID)) {
                // easy way
                controller.removeById(ID, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    public long receiveId(String path, String requestPathId){
        if (path.equals(requestPathId)) {
            return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
        }else return 0;
    }
}