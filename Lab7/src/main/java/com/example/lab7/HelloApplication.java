package com.example.lab7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        // Подключаем файл стилей для тёмной и светлой темы (Вариант 6)
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Редактор заметок — Лабораторная работа 7 (Вариант 6)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}