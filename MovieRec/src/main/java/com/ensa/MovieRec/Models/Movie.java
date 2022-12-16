package com.ensa.MovieRec.Models;

public class Movie implements Comparable<Movie> {
    private final int id;
    private final double score;
    private String name;
    private String imdbID;

    public Movie(int movieID, double score) {
        this.id = movieID;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    @Override
    public int compareTo(Movie M) {
        return Double.compare(getScore(), M.getScore());
    }
}
