package com.mymovies.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CustomFilmIdSequence {

    @Id
    private String id;

    private long nextId;

    public CustomFilmIdSequence() {
    }

    public CustomFilmIdSequence(String id, long nextId) {
        this.id = id;
        this.nextId = nextId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNextId() {
        return nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }
}
