package com.example.lab_2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

    @FXML private TextField txtTitle;
    @FXML private TextField txtGenre;
    @FXML private TextField txtYear;
    @FXML private TextField txtRating;
    @FXML private ComboBox<String> cmbAgeRating;

    @FXML private ComboBox<String> cmbCity;
    @FXML private RadioButton rb2D;
    @FXML private RadioButton rb3D;
    @FXML private CheckBox chkSubtitle;

    @FXML private Label lblResult;

    private ToggleGroup formatGroup;

    @FXML
    public void initialize() {
        cmbAgeRating.getItems().addAll("0+", "6+", "12+", "16+", "18+");
        cmbAgeRating.getSelectionModel().selectFirst();

        cmbCity.getItems().addAll("Алматы", "Астана", "Шымкент", "Караганда", "Актобе");
        cmbCity.getSelectionModel().selectFirst();

        formatGroup = new ToggleGroup();
        rb2D.setToggleGroup(formatGroup);
        rb3D.setToggleGroup(formatGroup);
    }

    @FXML
    private void onCreateClick() {
        String title = txtTitle.getText().trim();
        String genre = txtGenre.getText().trim();
        String yearStr = txtYear.getText().trim();
        String ratingStr = txtRating.getText().trim();
        String ageRating = cmbAgeRating.getValue();
        String city = cmbCity.getValue();

        if (title.isBlank() || genre.isBlank() || yearStr.isBlank() || ratingStr.isBlank()) {
            showError("Заполните все текстовые поля.");
            return;
        }

        int year;
        try {
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            showError("Год должен быть целым числом.");
            return;
        }

        if (year < 1895 || year > 2030) {
            showError("Введите корректный год выпуска (1895–2030).");
            return;
        }

        double rating;
        try {
            rating = Double.parseDouble(ratingStr);
        } catch (NumberFormatException e) {
            showError("Рейтинг должен быть числом (например, 8.5).");
            return;
        }

        if (rating < 0.0 || rating > 10.0) {
            showError("Рейтинг должен быть в диапазоне от 0 до 10.");
            return;
        }

        String format = rb2D.isSelected() ? "2D" : "3D / IMAX";
        String subtitles = chkSubtitle.isSelected() ? "Да" : "Нет";

        lblResult.setText(String.format(
                "Фильм: %s\nЖанр: %s\nГод: %d\nРейтинг: %.1f/10\nЦенз: %s\nГород: %s\nФормат: %s\nСубтитры: %s",
                title, genre, year, rating, ageRating, city, format, subtitles
        ));
    }

    @FXML
    private void onClearClick() {
        txtTitle.clear();
        txtGenre.clear();
        txtYear.clear();
        txtRating.clear();
        cmbAgeRating.getSelectionModel().selectFirst();
        cmbCity.getSelectionModel().selectFirst();
        rb2D.setSelected(true);
        chkSubtitle.setSelected(false);
        lblResult.setText("");
        txtTitle.requestFocus();
    }

    @FXML
    private void onExitClick() {
        Platform.exit();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}