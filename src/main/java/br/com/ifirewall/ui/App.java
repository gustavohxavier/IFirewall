package br.com.ifirewall.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("MainView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        stage.setTitle("IFirewall – Gerador de regras de filtragem");
        stage.setScene(scene);
        stage.show();
    }
}