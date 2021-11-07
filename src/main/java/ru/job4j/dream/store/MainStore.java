package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class MainStore {
    public static void main(String[] args) {
        Store store = DbStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        int id = 0;
        for (Post post : store.findAllPosts()) {
            id = post.getId();
            System.out.println(post);
        }
        System.out.println(store.findPostById(id));
        store.savePost(new Post(id, "Java Job 1"));
        System.out.println(store.findPostById(id));
        store.removePost(id);
        for (Post post : store.findAllPosts()) {
            System.out.println(id + " " + post.getName());
        }

        store.saveCandidate(new Candidate(0, "Java Job candidate"));
        for (Candidate candidate : store.findAllCandidates()) {
            id = candidate.getId();
            System.out.println(candidate);
        }
        System.out.println(store.findCandidateById(id));
        store.saveCandidate(new Candidate(id, "Java Job candidate 1"));
        System.out.println(store.findCandidateById(id));
        store.removeCandidate(id);
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(id + " " + candidate.getName());
        }
    }
}