package kz.atu.lab11.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kz.atu.lab11.model.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MovieService {
    private final ObservableList<Movie> movies = FXCollections.observableArrayList();

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public void addMovie(String title, String genre, double rating) {
        validate(title, genre, rating);
        String cleanTitle = title.trim();
        String cleanGenre = genre.trim();

        if (existsDuplicate(cleanTitle, cleanGenre, null)) {
            throw new IllegalArgumentException("Фильм «" + cleanTitle + "» в жанре «" + cleanGenre + "» уже есть в каталоге.");
        }
        movies.add(new Movie(cleanTitle, cleanGenre, round(rating)));
    }

    public void updateMovie(Movie movie, String title, String genre, double rating) {
        if (movie == null) {
            throw new IllegalArgumentException("Выберите фильм в таблице для редактирования.");
        }
        validate(title, genre, rating);
        String cleanTitle = title.trim();
        String cleanGenre = genre.trim();

        if (existsDuplicate(cleanTitle, cleanGenre, movie)) {
            throw new IllegalArgumentException("Такой фильм уже существует в каталоге.");
        }
        movie.setTitle(cleanTitle);
        movie.setGenre(cleanGenre);
        movie.setRating(round(rating));
    }

    public void deleteMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Выберите фильм в таблице для удаления.");
        }
        movies.remove(movie);
    }

    public double calculateAverageRating(List<Movie> list) {
        if (list == null || list.isEmpty()) return 0.0;
        return list.stream()
                .mapToDouble(Movie::getRating)
                .average()
                .orElse(0.0);
    }

    public Optional<Movie> getTopMovie(List<Movie> list) {
        if (list == null || list.isEmpty()) return Optional.empty();
        return list.stream().max(Comparator.comparingDouble(Movie::getRating));
    }

    private boolean existsDuplicate(String title, String genre, Movie ignoreMovie) {
        return movies.stream().anyMatch(m ->
                m != ignoreMovie &&
                        m.getTitle().equalsIgnoreCase(title) &&
                        m.getGenre().equalsIgnoreCase(genre)
        );
    }

    private void validate(String title, String genre, double rating) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Введите название фильма.");
        }
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Укажите жанр фильма.");
        }
        if (!Double.isFinite(rating) || rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Рейтинг должен быть числом от 0.0 до 10.0.");
        }
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
