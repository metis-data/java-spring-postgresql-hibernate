package io.metisdata.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name="title_ratings")
public class TitleRating {
    @Id
    private String tconst;
    private double averageRating;
    private long numVotes;

    public TitleRating(){
    }

    public String getTconst() {
        return tconst;
    }

    public double getAverageRating(){
        return averageRating;
    }

    public long getNumVotes(){
        return numVotes;
    }
}