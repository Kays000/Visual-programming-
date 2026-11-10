package kz.atu.lab11.model;

import javafx.beans.property.*;

public class Movie {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty genre = new SimpleStringProperty();
    private final DoubleProperty rating = new SimpleDoubleProperty();

    public Movie(String title, String genre, double rating) {
        setTitle(title);
        setGenre(genre);
        setRating(rating);
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }

    public String getGenre() { return genre.get(); }
    public void setGenre(String value) { genre.set(value); }
    public StringProperty genreProperty() { return genre; }

    public double getRating() { return rating.get(); }
    public void setRating(double value) { rating.set(value); }
    public DoubleProperty ratingProperty() { return rating; }
}
