package com.example.Dictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class DictionaryApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the GUI layout
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gammatest.fxml")));

        // Create a new Scene with the loaded layout
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Configure the primary stage
        primaryStage.setResizable(false); // Set stage to be non-resizable

        // Set the title of the application window
        primaryStage.setTitle("Dictionary Group ABCD");

        // Display the primary stage
        primaryStage.show();
    }
}
