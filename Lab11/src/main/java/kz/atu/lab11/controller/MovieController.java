package kz.atu.lab11.controller;
import kz.atu.lab11.service.MovieService;


import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import kz.atu.lab11.model.Movie;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieController {
    @FXML private TableView<Movie> tableMovies;
    @FXML private TableColumn<Movie, String> colTitle, colGenre;
    @FXML private TableColumn<Movie, Number> colRating;

    @FXML private TextField txtTitle, txtGenre, txtRating, txtSearch;
    @FXML private Slider sliderRating;
    @FXML private ComboBox<String> cmbGenreFilter;

    @FXML private Label lblCount, lblAvgRating, lblTopMovie;
    @FXML private Label lblPreviewTitle, lblPreviewGenre, lblPreviewRating, lblStatus;
    @FXML private Button btnUpdate, btnDelete;
    @FXML private VBox rootBox;

    private final MovieService service = new MovieService();
    private FilteredList<Movie> filtered;
    private boolean updatingSlider = false;

    @FXML
    private void initialize() {
        setupTableColumns();
        loadInitialData();

        filtered = new FilteredList<>(service.getMovies(), m -> true);
        SortedList<Movie> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(tableMovies.comparatorProperty());
        tableMovies.setItems(sorted);

        refreshGenreFilter();
        setupListeners();
        setupKeyboardShortcuts();

        updateSelectionState(null);
        updateStats();
    }

    private void setupTableColumns() {
        colTitle.setCellValueFactory(c -> c.getValue().titleProperty());
        colGenre.setCellValueFactory(c -> c.getValue().genreProperty());
        colRating.setCellValueFactory(c -> c.getValue().ratingProperty());

        // Динамическая подсветка рейтинга в ячейках таблицы
        colRating.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    double val = item.doubleValue();
                    setText(String.format("★ %.1f", val));
                    if (val >= 8.0) {
                        setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                    } else if (val >= 6.0) {
                        setStyle("-fx-text-fill: #d97706; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void loadInitialData() {
        service.addMovie("Начало", "Фантастика", 8.8);
        service.addMovie("Побег из Шоушенка", "Драма", 9.3);
        service.addMovie("Интерстеллар", "Фантастика", 8.7);
        service.addMovie("Тёмный рыцарь", "Боевик", 9.0);
        service.addMovie("1+1", "Комедия", 8.8);
        service.addMovie("Дюна: Часть вторая", "Фантастика", 8.5);
    }

    private void setupListeners() {
        // Живой поиск и фильтрация
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        cmbGenreFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Синхронизация слайдера и текстового поля рейтинга
        sliderRating.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!updatingSlider) {
                updatingSlider = true;
                txtRating.setText(String.format("%.1f", newVal.doubleValue()).replace(',', '.'));
                updatingSlider = false;
            }
        });

        txtRating.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!updatingSlider && newVal != null && !newVal.isBlank()) {
                try {
                    double val = Double.parseDouble(newVal.trim().replace(',', '.'));
                    if (val >= 0 && val <= 10) {
                        updatingSlider = true;
                        sliderRating.setValue(val);
                        updatingSlider = false;
                    }
                } catch (NumberFormatException ignored) {}
            }
        });

        // Реакция на выбор строки в таблице
        tableMovies.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            updateSelectionState(newSel);
        });
    }

    private void setupKeyboardShortcuts() {
        rootBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onClearSelection();
            } else if (event.getCode() == KeyCode.DELETE && tableMovies.isFocused()) {
                onDelete();
            }
        });
    }

    private void updateSelectionState(Movie selected) {
        boolean hasSelection = selected != null;
        btnUpdate.setDisable(!hasSelection);
        btnDelete.setDisable(!hasSelection);

        if (hasSelection) {
            txtTitle.setText(selected.getTitle());
            txtGenre.setText(selected.getGenre());
            txtRating.setText(String.valueOf(selected.getRating()));

            lblPreviewTitle.setText(selected.getTitle());
            lblPreviewGenre.setText("Жанр: " + selected.getGenre());
            lblPreviewRating.setText(String.format("★ %.1f / 10", selected.getRating()));
        } else {
            lblPreviewTitle.setText("Фильм не выбран");
            lblPreviewGenre.setText("Нажмите на строку в таблице");
            lblPreviewRating.setText("★ —");
        }
    }

    private void refreshGenreFilter() {
        String current = cmbGenreFilter.getValue();
        List<String> genres = service.getMovies().stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cmbGenreFilter.getItems().setAll("Все жанры");
        cmbGenreFilter.getItems().addAll(genres);

        if (current != null && cmbGenreFilter.getItems().contains(current)) {
            cmbGenreFilter.setValue(current);
        } else {
            cmbGenreFilter.setValue("Все жанры");
        }
    }

    private void applyFilter() {
        String query = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
        String genre = cmbGenreFilter.getValue();

        filtered.setPredicate(m -> {
            boolean matchesQuery = query.isEmpty()
                    || m.getTitle().toLowerCase().contains(query)
                    || m.getGenre().toLowerCase().contains(query);
            boolean matchesGenre = genre == null
                    || genre.equals("Все жанры")
                    || m.getGenre().equals(genre);
            return matchesQuery && matchesGenre;
        });
        updateStats();
    }

    private void updateStats() {
        lblCount.setText("Показано: " + filtered.size() + " из " + service.getMovies().size());
        double avg = service.calculateAverageRating(filtered);
        lblAvgRating.setText(String.format("Средний рейтинг: %.2f", avg));

        Optional<Movie> top = service.getTopMovie(filtered);
        lblTopMovie.setText(top.map(m -> "Лидер: " + m.getTitle() + " (" + m.getRating() + ")")
                .orElse("Лидер: —"));
    }

    private double readRating() {
        try {
            return Double.parseDouble(txtRating.getText().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Введите корректный числовой рейтинг.");
        }
    }

    @FXML
    private void onAdd() {
        try {
            service.addMovie(txtTitle.getText(), txtGenre.getText(), readRating());
            refreshGenreFilter();
            applyFilter();
            onClearSelection();
            setStatus("Фильм успешно добавлен!", false);
        } catch (IllegalArgumentException e) {
            setStatus(e.getMessage(), true);
        }
    }

    @FXML
    private void onUpdate() {
        try {
            Movie selected = tableMovies.getSelectionModel().getSelectedItem();
            service.updateMovie(selected, txtTitle.getText(), txtGenre.getText(), readRating());
            refreshGenreFilter();
            applyFilter();
            updateSelectionState(selected);
            tableMovies.refresh();
            setStatus("Изменения сохранены!", false);
        } catch (IllegalArgumentException e) {
            setStatus(e.getMessage(), true);
        }
    }

    @FXML
    private void onDelete() {
        Movie selected = tableMovies.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Сначала выберите фильм в таблице.", true);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Удаление фильма");
        confirm.setHeaderText("Удалить «" + selected.getTitle() + "»?");
        confirm.setContentText("Это действие нельзя отменить.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            service.deleteMovie(selected);
            refreshGenreFilter();
            applyFilter();
            onClearSelection();
            setStatus("Фильм удалён из каталога.", false);
        }
    }

    @FXML
    private void onResetFilters() {
        txtSearch.clear();
        cmbGenreFilter.setValue("Все жанры");
        setStatus("Фильтры сброшены.", false);
    }

    @FXML
    private void onClearSelection() {
        txtTitle.clear();
        txtGenre.clear();
        txtRating.setText("7.5");
        sliderRating.setValue(7.5);
        tableMovies.getSelectionModel().clearSelection();
    }
    private void setStatus(String text, boolean isError) {
        lblStatus.setText(text);
        lblStatus.setStyle(isError
                ? "-fx-text-fill: #dc2626; -fx-font-weight: bold;"
                : "-fx-text-fill: #16a34a; -fx-font-weight: bold;");
    }
}

