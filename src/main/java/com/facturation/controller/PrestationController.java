package com.facturation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.facturation.model.Prestation;
import com.facturation.model.Client;
import com.facturation.dao.ClientDAO;
import com.facturation.dao.PrestationDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PrestationController {
    
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private TextField entrepriseField;
    @FXML private VBox formationFields;
    @FXML private VBox consultationFields;
    @FXML private TextField moduleField;
    @FXML private TextField classeField;
    @FXML private TextField heureDebutField;
    @FXML private TextField heureFinField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField tjmField;
    @FXML private Label montantLabel;
    @FXML private Label messageLabel;
    
    private ClientDAO clientDAO = new ClientDAO();
    private PrestationDAO prestationDAO = new PrestationDAO();
    private double montantCalcule = 0.0;
    
    @FXML
    private void initialize() {
        // Initialiser les types de prestation
        ObservableList<String> types = FXCollections.observableArrayList("formation", "consultation");
        typeComboBox.setItems(types);
        
        // Initialiser la date à aujourd'hui
        datePicker.setValue(LocalDate.now());
        
        // Charger les clients
        chargerClients();
        
        // Écouter les changements de type
        typeComboBox.setOnAction(e -> onTypeChanged());
        
        // Écouter les changements de client
        clientComboBox.setOnAction(e -> onClientChanged());
        
        // Écouter les changements pour le calcul automatique
        heureDebutField.textProperty().addListener((obs, old, newValue) -> calculerMontant());
        heureFinField.textProperty().addListener((obs, old, newValue) -> calculerMontant());
        tjmField.textProperty().addListener((obs, old, newValue) -> calculerMontant());
    }
    
    private void chargerClients() {
        List<Client> clients = clientDAO.getAll();
        ObservableList<Client> clientList = FXCollections.observableArrayList(clients);
        clientComboBox.setItems(clientList);
    }
    
    private void onTypeChanged() {
        String type = typeComboBox.getValue();
        if ("formation".equals(type)) {
            formationFields.setVisible(true);
            consultationFields.setVisible(false);
        } else if ("consultation".equals(type)) {
            formationFields.setVisible(false);
            consultationFields.setVisible(true);
        }
        calculerMontant();
    }
    
    private void onClientChanged() {
        Client client = clientComboBox.getValue();
        if (client != null) {
            entrepriseField.setText(client.getEntreprise());
        }
    }
    
    @FXML
    private void handleCalculer() {
        calculerMontant();
        montantLabel.setText(String.format("%.2f €", montantCalcule));
    }
    
    private void calculerMontant() {
        String type = typeComboBox.getValue();
        
        if ("formation".equals(type)) {
            calculerMontantFormation();
        } else if ("consultation".equals(type)) {
            calculerMontantConsultation();
        }
        
        montantLabel.setText(String.format("%.2f €", montantCalcule));
    }
    
    private void calculerMontantFormation() {
        try {
            String heureDebut = heureDebutField.getText();
            String heureFin = heureFinField.getText();
            
            if (!heureDebut.isEmpty() && !heureFin.isEmpty()) {
                LocalTime debut = LocalTime.parse(heureDebut, DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime fin = LocalTime.parse(heureFin, DateTimeFormatter.ofPattern("HH:mm"));
                
                long heures = java.time.Duration.between(debut, fin).toHours();
                montantCalcule = heures * 70.0; // 70€ par heure
            }
        } catch (DateTimeParseException e) {
            montantCalcule = 0.0;
        }
    }
    
    private void calculerMontantConsultation() {
        try {
            String tjmText = tjmField.getText();
            if (!tjmText.isEmpty()) {
                double tjm = Double.parseDouble(tjmText);
                montantCalcule = tjm * 1.0; // 1 jour par défaut
            }
        } catch (NumberFormatException e) {
            montantCalcule = 0.0;
        }
    }
    
    @FXML
    private void handleEnregistrer() {
        if (validerFormulaire()) {
            Prestation prestation = creerPrestation();
            
            if (prestationDAO.creer(prestation)) {
                afficherMessage("Prestation enregistrée avec succès !", "success");
                viderFormulaire();
            } else {
                afficherMessage("Erreur lors de l'enregistrement", "error");
            }
        }
    }
    
    private boolean validerFormulaire() {
        if (typeComboBox.getValue() == null) {
            afficherMessage("Veuillez sélectionner un type de prestation", "error");
            return false;
        }
        
        if (datePicker.getValue() == null) {
            afficherMessage("Veuillez sélectionner une date", "error");
            return false;
        }
        
        if (clientComboBox.getValue() == null) {
            afficherMessage("Veuillez sélectionner un client", "error");
            return false;
        }
        
        String type = typeComboBox.getValue();
        if ("formation".equals(type)) {
            if (moduleField.getText().trim().isEmpty()) {
                afficherMessage("Veuillez saisir le module de formation", "error");
                return false;
            }
            if (heureDebutField.getText().trim().isEmpty() || heureFinField.getText().trim().isEmpty()) {
                afficherMessage("Veuillez saisir les heures de début et de fin", "error");
                return false;
            }
        } else if ("consultation".equals(type)) {
            if (descriptionArea.getText().trim().isEmpty()) {
                afficherMessage("Veuillez saisir la description", "error");
                return false;
            }
            if (tjmField.getText().trim().isEmpty()) {
                afficherMessage("Veuillez saisir le TJM", "error");
                return false;
            }
        }
        
        return true;
    }
    
    private Prestation creerPrestation() {
        Prestation prestation = new Prestation();
        prestation.setType(typeComboBox.getValue());
        prestation.setDate(datePicker.getValue());
        prestation.setClientId(clientComboBox.getValue().getId());
        prestation.setEntreprise(entrepriseField.getText());
        prestation.setMontantCalcule(montantCalcule);
        
        String type = typeComboBox.getValue();
        if ("formation".equals(type)) {
            prestation.setModule(moduleField.getText());
            prestation.setClasse(classeField.getText());
            try {
                prestation.setHeureDebut(LocalTime.parse(heureDebutField.getText(), DateTimeFormatter.ofPattern("HH:mm")));
                prestation.setHeureFin(LocalTime.parse(heureFinField.getText(), DateTimeFormatter.ofPattern("HH:mm")));
            } catch (DateTimeParseException e) {
                // Gérer l'erreur
            }
        } else if ("consultation".equals(type)) {
            prestation.setDescription(descriptionArea.getText());
            try {
                prestation.setTjm(Double.parseDouble(tjmField.getText()));
            } catch (NumberFormatException e) {
                // Gérer l'erreur
            }
        }
        
        return prestation;
    }
    
    private void viderFormulaire() {
        typeComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        clientComboBox.setValue(null);
        entrepriseField.clear();
        moduleField.clear();
        classeField.clear();
        heureDebutField.clear();
        heureFinField.clear();
        descriptionArea.clear();
        tjmField.clear();
        montantLabel.setText("0.00 €");
        montantCalcule = 0.0;
    }
    
    @FXML
    private void handleAnnuler() {
        viderFormulaire();
    }
    
    @FXML
    private void handleRetour() {
        Stage stage = (Stage) typeComboBox.getScene().getWindow();
        stage.close();
    }
    
    private void afficherMessage(String message, String type) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
        
        if ("error".equals(type)) {
            messageLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
        } else {
            messageLabel.setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
        }
        
        // Masquer le message après 3 secondes
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> messageLabel.setVisible(false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
} 