package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore {

    private static AtomicInteger postID = new AtomicInteger(4);

    private static AtomicInteger candidateId = new AtomicInteger(4);

    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "easy"));
        posts.put(2, new Post(2, "Middle Java Job", "medium"));
        posts.put(3, new Post(3, "Senior Java Job", "hard"));
        Candidate juniorJava = new Candidate(1, "Junior Java");
        Candidate middleJava = new Candidate(2, "Middle Java");
        juniorJava.setFileName("1.jpg");
        middleJava.setFileName("2.jpg");
        candidates.put(1, juniorJava);
        candidates.put(2, middleJava);
        candidates.put(3, new Candidate(3, "Senior Java"));

    }

    public static MemStore instOf() {
        return INST;
    }

    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(postID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(candidateId.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void removeCandidate(int id) {
        candidates.remove(id);
    }

}