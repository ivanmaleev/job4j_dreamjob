package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DbStore implements Store {

    private static String storePath;

    private static final DbStore INSTANCE = new DbStore();

    private static final Logger LOG = LogManager.getLogger(DbStore.class.getName());

    private final BasicDataSource pool = new BasicDataSource();

    private DbStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        DbStore.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            LOG.error("Invalid parameters", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            LOG.error("Driver not loaded", e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new DbStore();
    }

    public static String getStorePath() {
        if (storePath == null) {
            Properties cfg = new Properties();
            try (BufferedReader io = new BufferedReader(
                    new InputStreamReader(
                            DbStore.class.getClassLoader()
                                    .getResourceAsStream("db.properties")
                    )
            )) {
                cfg.load(io);
            } catch (Exception e) {
                LOG.error("Invalid parameters", e);
            }
            storePath = cfg.getProperty("storepath");
        }
        return storePath;
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public List<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created")));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return posts;
    }

    @Override
    public List<Post> findAllTodayPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM post where created >= 'today'")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created")));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return posts;
    }

    @Override
    public List<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT cand.id, cand.name, cand.filename, "
                             + "cand.city_id, c.name as cityname, cand.created from "
                             + "candidate as cand left join city as c "
                             + "on cand.city_id = c.id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getString("filename"),
                            it.getInt("city_id"),
                            it.getString("cityname"),
                            it.getTimestamp("created")));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return candidates;
    }

    public List<Candidate> findAllTodayCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT cand.id, cand.name, cand.filename, "
                             + "cand.city_id, c.name as cityname from "
                             + "candidate as cand left join city as c "
                             + "on cand.city_id = c.id where created >= 'today'")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getString("filename"),
                            it.getInt("city_id"),
                            it.getString("cityname")));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidate SET name = (?), filename = (?), "
                             + "city_id = (?) WHERE id = (?)")
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getFileName());
            ps.setInt(3, candidate.getCityID());
            ps.setInt(4, candidate.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
    }

    private void createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO candidate(name, filename, city_id) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getFileName());
            ps.setInt(3, candidate.getCityID());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
    }

    private Post createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO post(name, description, created) VALUES (?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, post.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return post;
    }

    private void updatePost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE post SET name = (?), description = (?) WHERE id = (?)")
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setInt(3, post.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
    }

    @Override
    public Post findPostById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Post(it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created"));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT cand.id, cand.name, cand.filename, "
                             + "cand.city_id, c.name as cityname, cand.created from "
                             + "candidate as cand left join city as c "
                             + "on cand.city_id = c.id WHERE cand.id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getString("filename"),
                            it.getInt("city_id"),
                            it.getString("cityname"),
                            it.getTimestamp("created"));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return null;
    }

    @Override
    public void removeCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
    }

    @Override
    public void removePost(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
    }

    @Override
    public User saveUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return user;
    }

    @Override
    public void removeUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users WHERE email = ?")
        ) {
            ps.setString(1, user.getEmail());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
    }

    @Override
    public User findUserByEmail(String email) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE email = ?")
        ) {
            ps.setString(1, email);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    User user = new User();
                    user.setName(it.getString("name"));
                    user.setPassword(it.getString("password"));
                    user.setEmail(it.getString("email"));
                    user.setId(it.getInt("id"));
                    return user;
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return null;
    }

    @Override
    public List<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(it.getInt("id"),
                            it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return cities;
    }

    @Override
    public City findCityById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new City(it.getInt("id"),
                            it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return null;
    }

    @Override
    public City findCityByName(String name) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM city WHERE name = ?")
        ) {
            ps.setString(1, name);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new City(it.getInt("id"),
                            it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOG.error("Connection to DB not established", e);
        }
        return null;
    }
}