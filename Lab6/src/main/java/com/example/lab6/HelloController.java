package com.example.lab6;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class HelloController {

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbFilterGenre;
    @FXML private ListView<String> listGenres;

    @FXML private TableView<Game> tableGames;
    @FXML private TableColumn<Game, String> colTitle;
    @FXML private TableColumn<Game, String> colGenre;
    @FXML private TableColumn<Game, String> colPlatform;
    @FXML private TableColumn<Game, Double> colRating;

    @FXML private TextField txtTitle;
    @FXML private ComboBox<String> cmbGenre;
    @FXML private TextField txtPlatform;
    @FXML private TextField txtRating;

    @FXML private Label lblCount;

    private final ObservableList<Game> games = FXCollections.observableArrayList();
    private FilteredList<Game> filteredGames;

    @FXML
    public void initialize() {
        // Настройка колонок таблицы
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        colPlatform.setCellValueFactory(new PropertyValueFactory<>("platform"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        // Тестовые данные
        games.addAll(
                new Game("The Witcher 3", "RPG", "PC", 9.8),
                new Game("Elden Ring", "RPG", "PlayStation", 9.6),
                new Game("GTA V", "Экшен", "PC", 9.5),
                new Game("CS:GO / CS2", "Шутер", "PC", 8.8),
                new Game("FIFA 24", "Спорт", "Xbox", 7.5)
        );

        // Наполнение ComboBox и ListView
        cmbGenre.getItems().addAll("RPG", "Экшен", "Шутер", "Спорт", "Стратегия");
        cmbFilterGenre.getItems().addAll("Все", "RPG", "Экшен", "Шутер", "Спорт", "Стратегия");
        cmbFilterGenre.setValue("Все");

        listGenres.getItems().addAll("Все игры", "RPG", "Экшен", "Шутер", "Спорт", "Стратегия");

        // Синхронизация ListView и ComboBox
        listGenres.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbFilterGenre.setValue(newVal.equals("Все игры") ? "Все" : newVal);
                applyFilter();
            }
        });

        // Фильтрация
        filteredGames = new FilteredList<>(games, p -> true);
        tableGames.setItems(filteredGames);

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        cmbFilterGenre.setOnAction(event -> applyFilter());

        // Перенос данных строки в форму при клике
        tableGames.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                txtTitle.setText(selected.getTitle());
                cmbGenre.setValue(selected.getGenre());
                txtPlatform.setText(selected.getPlatform());
                txtRating.setText(String.valueOf(selected.getRating()));
            }
        });

        updateCount();
    }

    private void applyFilter() {
        String search = txtSearch.getText().trim().toLowerCase();
        String genre = cmbFilterGenre.getValue();

        filteredGames.setPredicate(game -> {
            boolean matchesSearch = game.getTitle().toLowerCase().contains(search) ||
                    game.getPlatform().toLowerCase().contains(search);
            boolean matchesGenre = genre == null || genre.equals("Все") || game.getGenre().equals(genre);
            return matchesSearch && matchesGenre;
        });

        updateCount();
    }

    @FXML
    private void onAddClick() {
        if (!validateInput()) return;

        try {
            String title = txtTitle.getText().trim();
            String genre = cmbGenre.getValue();
            String platform = txtPlatform.getText().trim();
            double rating = Double.parseDouble(txtRating.getText().trim());

            if (rating < 0 || rating > 10) {
                showError("Рейтинг должен быть от 0 до 10.");
                return;
            }

            games.add(new Game(title, genre, platform, rating));
            clearInput();
            applyFilter();
        } catch (NumberFormatException e) {
            showError("Рейтинг должен быть числом.");
        }
    }

    @FXML
    private void onEditClick() {
        Game selected = tableGames.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Выберите игру для изменения.");
            return;
        }

        if (!validateInput()) return;

        try {
            double rating = Double.parseDouble(txtRating.getText().trim());
            if (rating < 0 || rating > 10) {
                showError("Рейтинг должен быть от 0 до 10.");
                return;
            }

            selected.setTitle(txtTitle.getText().trim());
            selected.setGenre(cmbGenre.getValue());
            selected.setPlatform(txtPlatform.getText().trim());
            selected.setRating(rating);

            tableGames.refresh();
            clearInput();
            applyFilter();
        } catch (NumberFormatException e) {
            showError("Рейтинг должен быть числом.");
        }
    }

    @FXML
    private void onDeleteClick() {
        Game selected = tableGames.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Выберите игру для удаления.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Удалить запись " + selected.getTitle() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            games.remove(selected);
            clearInput();
            applyFilter();
        }
    }

    @FXML
    private void onClearFilterClick() {
        txtSearch.clear();
        cmbFilterGenre.setValue("Все");
        listGenres.getSelectionModel().select("Все игры");
        applyFilter();
    }

    private boolean validateInput() {
        if (txtTitle.getText().isBlank() || cmbGenre.getValue() == null ||
                txtPlatform.getText().isBlank() || txtRating.getText().isBlank()) {
            showError("Заполните все поля.");
            return false;
        }
        return true;
    }

    private void updateCount() {
        lblCount.setText("Найдено записей: " + tableGames.getItems().size());
    }

    private void clearInput() {
        txtTitle.clear();
        txtPlatform.clear();
        txtRating.clear();
        cmbGenre.getSelectionModel().clearSelection();
        tableGames.getSelectionModel().clearSelection();
        txtTitle.requestFocus();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}