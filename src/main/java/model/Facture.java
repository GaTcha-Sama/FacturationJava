package model;

import java.time.LocalDate;

public class Facture {
    private int id;
    private String numeroFacture;
    private int mois;
    private int annee;
    private double montantTotal;
    private String statut; // "en_attente", "payee", "annulee"
    private String fichierPdf;
    private int clientId;
    private String dateCreation;
    private String datePaiement;

    // Constructeurs
    public Facture() {}

    public Facture(String numeroFacture, int mois, int annee, double montantTotal, int clientId) {
        this.numeroFacture = numeroFacture;
        this.mois = mois;
        this.annee = annee;
        this.montantTotal = montantTotal;
        this.clientId = clientId;
        this.statut = "en_attente";
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroFacture() { return numeroFacture; }
    public void setNumeroFacture(String numeroFacture) { this.numeroFacture = numeroFacture; }

    public int getMois() { return mois; }
    public void setMois(int mois) { this.mois = mois; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public double getMontantTotal() { return montantTotal; }
    public void setMontantTotal(double montantTotal) { this.montantTotal = montantTotal; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getFichierPdf() { return fichierPdf; }
    public void setFichierPdf(String fichierPdf) { this.fichierPdf = fichierPdf; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }

    public String getDatePaiement() { return datePaiement; }
    public void setDatePaiement(String datePaiement) { this.datePaiement = datePaiement; }

    // Méthode pour générer un numéro de facture
    public static String genererNumeroFacture(int mois, int annee, int sequence) {
        return String.format("FAC-%04d-%02d-%03d", annee, mois, sequence);
    }

    // Méthode pour obtenir le statut en français
    public String getStatutFrancais() {
        switch (statut) {
            case "en_attente": return "En attente";
            case "payee": return "Payée";
            case "annulee": return "Annulée";
            default: return statut;
        }
    }

    @Override
    public String toString() {
        return numeroFacture + " - " + mois + "/" + annee + " - " + montantTotal + "€ - " + getStatutFrancais();
    }
} 