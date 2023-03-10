package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.config.JavaConfig;
import ru.netology.controller.PostController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String REQUEST_PATH = "/api/posts";
    private static final String REQUEST_PATH_WITH_ID = "/api/posts/\\d+";

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = (PostController) context.getBean("postController");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            final boolean pathWithId = path.matches(REQUEST_PATH_WITH_ID);
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