package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Prestation {
    private int id;
    private String type; // "formation" ou "consultation"
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private String description;
    private double tjm; // Taux Journalier Moyen
    private String module;
    private String classe;
    private String entreprise;
    private int clientId;
    private double montantCalcule;
    private String dateCreation;

    // Constructeurs
    public Prestation() {}

    public Prestation(String type, LocalDate date, int clientId) {
        this.type = type;
        this.date = date;
        this.clientId = clientId;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getTjm() { return tjm; }
    public void setTjm(double tjm) { this.tjm = tjm; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public double getMontantCalcule() { return montantCalcule; }
    public void setMontantCalcule(double montantCalcule) { this.montantCalcule = montantCalcule; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    // Méthode pour calculer le montant
    public void calculerMontant() {
        if ("formation".equals(type) && heureDebut != null && heureFin != null) {
            // Calcul pour formation : (heure fin - heure début) * taux horaire (70€/h par défaut)
            long heures = java.time.Duration.between(heureDebut, heureFin).toHours();
            this.montantCalcule = heures * 70.0; // 70€ par heure
        } else if ("consultation".equals(type)) {
            // Calcul pour consultation : TJM * nombre de jours (1 jour par défaut)
            this.montantCalcule = tjm * 1.0;
        }
    }

    @Override
    public String toString() {
        return type + " - " + date + " - " + entreprise + " (" + montantCalcule + "€)";
    }
} 