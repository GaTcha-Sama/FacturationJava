package com.facturation.dao;

import com.facturation.model.Prestation;
import com.facturation.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrestationDAO {
    
    // Créer une nouvelle prestation
    public boolean creer(Prestation prestation) {
        String sql = "INSERT INTO prestation (type, date, heure_debut, heure_fin, description, tjm, module, classe, entreprise, client_id, montant_calcule) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, prestation.getType());
            pstmt.setString(2, prestation.getDate().toString());
            
            if (prestation.getHeureDebut() != null) {
                pstmt.setString(3, prestation.getHeureDebut().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                pstmt.setNull(3, Types.VARCHAR);
            }
            
            if (prestation.getHeureFin() != null) {
                pstmt.setString(4, prestation.getHeureFin().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            
            pstmt.setString(5, prestation.getDescription());
            pstmt.setDouble(6, prestation.getTjm());
            pstmt.setString(7, prestation.getModule());
            pstmt.setString(8, prestation.getClasse());
            pstmt.setString(9, prestation.getEntreprise());
            pstmt.setInt(10, prestation.getClientId());
            pstmt.setDouble(11, prestation.getMontantCalcule());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de la prestation : " + e.getMessage());
            return false;
        }
    }
    
    // Récupérer toutes les prestations
    public List<Prestation> getAll() {
        List<Prestation> prestations = new ArrayList<>();
        String sql = "SELECT * FROM prestation ORDER BY date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                prestations.add(creerPrestationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des prestations : " + e.getMessage());
        }
        
        return prestations;
    }
    
    // Récupérer les prestations d'un client
    public List<Prestation> getByClientId(int clientId) {
        List<Prestation> prestations = new ArrayList<>();
        String sql = "SELECT * FROM prestation WHERE client_id = ? ORDER BY date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                prestations.add(creerPrestationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des prestations du client : " + e.getMessage());
        }
        
        return prestations;
    }
    
    // Récupérer les prestations d'un mois/année
    public List<Prestation> getByMoisAnnee(int mois, int annee) {
        List<Prestation> prestations = new ArrayList<>();
        String sql = "SELECT * FROM prestation WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ? ORDER BY date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, String.format("%02d", mois));
            pstmt.setString(2, String.valueOf(annee));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                prestations.add(creerPrestationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des prestations par mois/année : " + e.getMessage());
        }
        
        return prestations;
    }
    
    // Calculer le chiffre d'affaires d'un mois
    public double getChiffreAffairesMois(int mois, int annee) {
        String sql = "SELECT SUM(montant_calcule) FROM prestation WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, String.format("%02d", mois));
            pstmt.setString(2, String.valueOf(annee));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du chiffre d'affaires : " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // Calculer le chiffre d'affaires d'une année
    public double getChiffreAffairesAnnee(int annee) {
        String sql = "SELECT SUM(montant_calcule) FROM prestation WHERE strftime('%Y', date) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, String.valueOf(annee));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du chiffre d'affaires annuel : " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // Méthode utilitaire pour créer un objet Prestation à partir d'un ResultSet
    private Prestation creerPrestationFromResultSet(ResultSet rs) throws SQLException {
        Prestation prestation = new Prestation();
        prestation.setId(rs.getInt("id"));
        prestation.setType(rs.getString("type"));
        prestation.setDate(LocalDate.parse(rs.getString("date")));
        
        String heureDebut = rs.getString("heure_debut");
        if (heureDebut != null) {
            prestation.setHeureDebut(LocalTime.parse(heureDebut, DateTimeFormatter.ofPattern("HH:mm")));
        }
        
        String heureFin = rs.getString("heure_fin");
        if (heureFin != null) {
            prestation.setHeureFin(LocalTime.parse(heureFin, DateTimeFormatter.ofPattern("HH:mm")));
        }
        
        prestation.setDescription(rs.getString("description"));
        prestation.setTjm(rs.getDouble("tjm"));
        prestation.setModule(rs.getString("module"));
        prestation.setClasse(rs.getString("classe"));
        prestation.setEntreprise(rs.getString("entreprise"));
        prestation.setClientId(rs.getInt("client_id"));
        prestation.setMontantCalcule(rs.getDouble("montant_calcule"));
        prestation.setDateCreation(rs.getString("date_creation"));
        
        return prestation;
    }
} 