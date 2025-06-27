package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Utilisateur;

public class MenuController {
    
    @FXML
    private Label userLabel;
    
    private Utilisateur utilisateur;
    
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (userLabel != null) {
            userLabel.setText("Connecté en tant que : " + utilisateur.getNom());
        }
    }
    
    @FXML
    private void handleAddPrestation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Prestation.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Ajouter une Prestation");
            stage.setScene(new Scene(root, 600, 500));
            stage.setResizable(false);
            stage.show();
            
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir l'écran de prestation", e.getMessage());
        }
    }
    
    @FXML
    private void handleManageClients() {
        showAlert("Info", "Fonctionnalité", "Gestion des clients - À implémenter");
    }
    
    @FXML
    private void handleGenerateFacture() {
        showAlert("Info", "Fonctionnalité", "Génération de facture - À implémenter");
    }
    
    @FXML
    private void handleViewBilan() {
        showAlert("Info", "Fonctionnalité", "Bilan de chiffre d'affaires - À implémenter");
    }
    
    @FXML
    private void handleListPrestations() {
        showAlert("Info", "Fonctionnalité", "Liste des prestations - À implémenter");
    }
    
    @FXML
    private void handleListFactures() {
        showAlert("Info", "Fonctionnalité", "Liste des factures - À implémenter");
    }
    
    @FXML
    private void handleLogout() {
        try {
            // Retourner à l'écran de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) userLabel.getScene().getWindow();
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            stage.setTitle("Connexion - Gestion des Factures");
            stage.setResizable(false);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de se déconnecter", e.getMessage());
        }
    }
    
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 