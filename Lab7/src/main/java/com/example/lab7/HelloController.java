package com.example.lab7;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelloController {

    @FXML private MenuItem miNew;
    @FXML private MenuItem miOpen;
    @FXML private MenuItem miSave;
    @FXML private TextArea txtEditor;
    @FXML private Label lblStatus;

    private Path currentFile;

    @FXML
    public void initialize() {
        // Горячие клавиши
        miNew.setAccelerator(KeyCombination.keyCombination("Shortcut+N"));
        miOpen.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
        miSave.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));

        // Обновление строки состояния при вводе текста[span_3](start_span)[span_3](end_span)
        txtEditor.textProperty().addListener((obs, oldText, newText) ->
                lblStatus.setText("Символов: " + newText.length()));
    }

    @FXML
    private void onNew() {
        txtEditor.clear();
        currentFile = null;
        lblStatus.setText("Новый документ");
    }

    @FXML
    private void onOpen() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
        File file = chooser.showOpenDialog(txtEditor.getScene().getWindow());
        if (file == null) return;
        try {
            txtEditor.setText(Files.readString(file.toPath(), StandardCharsets.UTF_8));
            currentFile = file.toPath();
            lblStatus.setText("Открыт: " + file.getName());
        } catch (IOException ex) {
            showError("Ошибка открытия: " + ex.getMessage());
        }
    }

    @FXML
    private void onSave() {
        if (currentFile == null) {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
            File file = chooser.showSaveDialog(txtEditor.getScene().getWindow());
            if (file == null) return;
            currentFile = file.toPath();
        }
        try {
            Files.writeString(currentFile, txtEditor.getText(), StandardCharsets.UTF_8);
            lblStatus.setText("Сохранено: " + currentFile.getFileName());
        } catch (IOException ex) {
            showError("Ошибка сохранения: " + ex.getMessage());
        }
    }

    @FXML private void onCopy() { txtEditor.copy(); }
    @FXML private void onCut() { txtEditor.cut(); }
    @FXML private void onPaste() { txtEditor.paste(); }
    @FXML private void onExit() { Platform.exit(); }

    @FXML
    private void onAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION,
                "Редактор заметок. Лабораторная работа №7 (Вариант 6).", ButtonType.OK);
        a.setHeaderText("О программе");
        a.showAndWait();
    }

    // Индивидуальное задание: переключение тем (Вариант 6)[span_4](start_span)[span_4](end_span)
    @FXML
    private void onLightTheme() {
        txtEditor.getScene().getRoot().getStyleClass().remove("dark-theme");
        lblStatus.setText("Установлена светлая тема");
    }

    @FXML
    private void onDarkTheme() {
        if (!txtEditor.getScene().getRoot().getStyleClass().contains("dark-theme")) {
            txtEditor.getScene().getRoot().getStyleClass().add("dark-theme");
        }
        lblStatus.setText("Установлена тёмная тема");
    }

    private void showError(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        a.setHeaderText("Ошибка");
        a.showAndWait();
    }
}