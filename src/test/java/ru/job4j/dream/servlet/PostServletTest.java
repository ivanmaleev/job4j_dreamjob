package ru.job4j.dream.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostServletTest {

    private static Connection connection;

    @Before
    public void initConnection() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:h2:./testdb;MODE=PostgreSQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE;",
                    "",
                    ""
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void whenCreatePost() throws IOException, ServletException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("name of new post");
        when(req.getParameter("description")).thenReturn("d");
        new PostServlet().doPost(req, resp);
        Post post = DbStore.instOf().findPostById(1);
        assertThat(post, notNullValue());
    }

    @After
    public void refreshDB() {
        try (PreparedStatement statement = connection.prepareStatement(
                "ALTER TABLE post ALTER COLUMN id RESTART WITH 1; " +
                        "DELETE FROM post;")) {
            statement.execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}