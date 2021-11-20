package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.List;

public interface Store {
    List<Post> findAllPosts();

    List<Post> findAllTodayPosts();

    List<Candidate> findAllCandidates();

    List<Candidate> findAllTodayCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    void removeCandidate(int id);

    void removePost(int id);

    User saveUser(User user);

    void removeUser(User user);

    User findUserByEmail(String email);

    List<City> findAllCities();

    City findCityById(int id);

    City findCityByName(String name);
}