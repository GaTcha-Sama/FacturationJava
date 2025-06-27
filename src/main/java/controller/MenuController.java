package controller;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import dao.ClientDAO;
import dao.PrestationDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Client;
import model.Prestation;
import model.Utilisateur;

public class MenuController {
    
    @FXML
    private Label userLabel;
    
    private Utilisateur utilisateur;
    private PrestationDAO prestationDAO = new PrestationDAO();
    private ClientDAO clientDAO = new ClientDAO();
    
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
        try {
            // Récupérer tous les clients
            List<Client> clients = clientDAO.getAll();
            
            if (clients.isEmpty()) {
                showAlert("Information", "Aucun client", "Aucun client n'est enregistré. Veuillez d'abord ajouter des clients.");
                return;
            }
            
            // Créer un dialogue pour sélectionner le client
            ChoiceDialog<Client> dialog = new ChoiceDialog<>(clients.get(0), clients);
            dialog.setTitle("Générer une Facture");
            dialog.setHeaderText("Sélectionnez un client");
            dialog.setContentText("Client :");
            
            Optional<Client> result = dialog.showAndWait();
            
            if (result.isPresent()) {
                Client selectedClient = result.get();
                
                // Récupérer les prestations du client
                List<Prestation> prestations = prestationDAO.getByClientId(selectedClient.getId());
                
                if (prestations.isEmpty()) {
                    showAlert("Information", "Aucune prestation", 
                             "Aucune prestation trouvée pour le client " + selectedClient.getNom());
                    return;
                }
                
                // Calculer le montant total
                double montantTotal = prestations.stream()
                    .mapToDouble(Prestation::getMontantCalcule)
                    .sum();
                
                // Afficher un résumé
                StringBuilder message = new StringBuilder();
                message.append("Facture pour : ").append(selectedClient.getNom()).append("\n\n");
                message.append("Nombre de prestations : ").append(prestations.size()).append("\n");
                message.append("Montant total : ").append(String.format("%.2f", montantTotal)).append(" €\n\n");
                message.append("Prestations :\n");
                
                for (Prestation p : prestations) {
                    message.append("- ").append(p.getType()).append(" du ").append(p.getDate())
                           .append(" : ").append(String.format("%.2f", p.getMontantCalcule())).append(" €\n");
                }
                
                showAlert("Résumé de la Facture", "Facture générée avec succès", message.toString());
                
            }
            
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la génération de la facture", e.getMessage());
        }
    }
    
    @FXML
    private void handleViewBilan() {
        try {
            // Obtenir le mois et l'année actuels
            YearMonth currentMonth = YearMonth.now();
            int currentYear = currentMonth.getYear();
            int currentMonthValue = currentMonth.getMonthValue();
            
            // Calculer le chiffre d'affaires du mois en cours
            double caMois = prestationDAO.getChiffreAffairesMois(currentMonthValue, currentYear);
            
            // Calculer le chiffre d'affaires de l'année
            double caAnnee = prestationDAO.getChiffreAffairesAnnee(currentYear);
            
            // Récupérer toutes les prestations pour les statistiques
            List<Prestation> allPrestations = prestationDAO.getAll();
            
            // Compter les types de prestations
            long formations = allPrestations.stream().filter(p -> "formation".equals(p.getType())).count();
            long consultations = allPrestations.stream().filter(p -> "consultation".equals(p.getType())).count();
            
            // Créer le message du bilan
            StringBuilder message = new StringBuilder();
            message.append("BILAN DE CHIFFRE D'AFFAIRES\n\n");
            message.append("Période : ").append(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale.FRENCH))).append("\n\n");
            message.append("Chiffre d'affaires du mois : ").append(String.format("%.2f", caMois)).append(" €\n");
            message.append("Chiffre d'affaires de l'année : ").append(String.format("%.2f", caAnnee)).append(" €\n\n");
            message.append("STATISTIQUES GLOBALES\n");
            message.append("Total des prestations : ").append(allPrestations.size()).append("\n");
            message.append("Formations : ").append(formations).append("\n");
            message.append("Consultations : ").append(consultations).append("\n");
            
            if (!allPrestations.isEmpty()) {
                double moyenne = allPrestations.stream()
                    .mapToDouble(Prestation::getMontantCalcule)
                    .average()
                    .orElse(0.0);
                message.append("Montant moyen par prestation : ").append(String.format("%.2f", moyenne)).append(" €\n");
            }
            
            showAlert("Bilan de Chiffre d'Affaires", "Résumé financier", message.toString());
            
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du calcul du bilan", e.getMessage());
        }
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