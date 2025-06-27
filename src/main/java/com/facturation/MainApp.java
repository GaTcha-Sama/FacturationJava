package com.facturation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.facturation.util.DBConnection;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialiser la base de données
        DBConnection.initializeDatabase();
        
        // Charger l'écran de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();
        
        // Créer la scène avec le CSS
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        
        primaryStage.setTitle("Gestion des Factures - Micro-Entrepreneur");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 