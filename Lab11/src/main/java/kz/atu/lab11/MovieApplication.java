package kz.atu.lab11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MovieApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlUrl = MovieApplication.class.getResource("movie-view.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("Файл movie-view.fxml не найден в resources/kz/atu/lab11/");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load(), 920, 580);

        URL cssUrl = MovieApplication.class.getResource("style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("КиноКаталог — Lab11");
        stage.setMinWidth(820);
        stage.setMinHeight(520);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
