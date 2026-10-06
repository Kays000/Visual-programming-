package com.example.lab6;

public class Game {
    private String title;
    private String genre;
    private String platform;
    private double rating;

    public Game(String title, String genre, String platform, double rating) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}