package ru.job4j.dream.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private String fileName;
    private City city = new City(0, "");
    private Timestamp created;

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
        created = Timestamp.from(Instant.now());
    }

    public Candidate(int id, String name, String fileName) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        created = Timestamp.from(Instant.now());
    }

    public Candidate(int id, String name, String fileName, City city) {
        this(id, name, fileName, city, Timestamp.from(Instant.now()));
    }

    public Candidate(int id, String name, String fileName, City city, Timestamp created) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        this.city = city;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id
                && Objects.equals(name, candidate.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Candidate{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", fileName='" + fileName + '\''
                + '}';
    }
}