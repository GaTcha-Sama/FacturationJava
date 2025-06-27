package com.facturation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.facturation.dao.UtilisateurDAO;
import com.facturation.model.Utilisateur;

public class LoginController {
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        
        // Validation des champs
        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        // Tentative d'authentification
        Utilisateur utilisateur = utilisateurDAO.authentifier(email, password);
        
        if (utilisateur != null) {
            // Connexion réussie
            openMainMenu(utilisateur);
        } else {
            // Échec de connexion
            showError("Email ou mot de passe incorrect");
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void openMainMenu(Utilisateur utilisateur) {
        try {
            // Charger le menu principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
            Parent root = loader.load();
            
            // Passer l'utilisateur au contrôleur du menu
            MenuController menuController = loader.getController();
            menuController.setUtilisateur(utilisateur);
            
            // Créer la nouvelle scène
            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Menu Principal - Gestion des Factures");
            stage.setResizable(true);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le menu principal", e.getMessage());
        }
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Méthode pour initialiser les champs (appelée automatiquement par JavaFX)
    @FXML
    private void initialize() {
        // Effacer le message d'erreur quand l'utilisateur tape
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setVisible(false);
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setVisible(false);
        });
        
        // Permettre la connexion avec la touche Entrée
        passwordField.setOnAction(event -> handleLogin());
    }
} 