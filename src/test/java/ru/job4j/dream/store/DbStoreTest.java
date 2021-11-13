package ru.job4j.dream.store;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;

public class DbStoreTest {

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
    public void whenFindAllPosts() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        Collection<Post> colPosts = List.of(postInDb);
        Collection<Post> allPosts = store.findAllPosts();
        assertThat(allPosts, is(colPosts));
    }

    @Test
    public void whenCreatePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        post.setName("Java Job 1");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is("Java Job 1"));
    }

    @Test
    public void whenRemovePost() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        store.removePost(postInDb.getId());
        assertNull(store.findPostById(post.getId()));
    }


    @Test
    public void whenFindPostById() {
        Store store = DbStore.instOf();
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenFindAllCandidates() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Java Job");
        store.saveCandidate(candidate);
        Candidate candidateInDb = store.findCandidateById(candidate.getId());
        Collection<Candidate> colCandidates = List.of(candidateInDb);
        Collection<Candidate> allCandidates = store.findAllCandidates();
        assertThat(allCandidates, is(colCandidates));
    }

    @Test
    public void whenCreateCandidates() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Java Job");
        store.saveCandidate(candidate);
        Candidate candidateInDb = store.findCandidateById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @Test
    public void whenUpdateCandidate() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Java Job");
        store.saveCandidate(candidate);
        candidate.setName("Java Job 1");
        store.saveCandidate(candidate);
        Candidate postInDb = store.findCandidateById(candidate.getId());
        assertThat(postInDb.getName(), is("Java Job 1"));
    }

    @Test
    public void whenRemoveCandidate() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Java Job");
        store.saveCandidate(candidate);
        Candidate candidateInDb = store.findCandidateById(candidate.getId());
        store.removeCandidate(candidateInDb.getId());
        assertNull(store.findCandidateById(candidate.getId()));
    }


    @Test
    public void whenFindCandidateById() {
        Store store = DbStore.instOf();
        Candidate candidate = new Candidate(0, "Java Job");
        store.saveCandidate(candidate);
        Candidate candidateInDb = store.findCandidateById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
    }

    @After
    public void refreshDB() {
        try (PreparedStatement statement = connection.prepareStatement("delete from post")) {
            statement.execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try (PreparedStatement statement = connection.prepareStatement("delete from candidate")) {
            statement.execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}