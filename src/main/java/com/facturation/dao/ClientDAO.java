package com.facturation.dao;

import com.facturation.model.Client;
import com.facturation.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    
    // Créer un nouveau client
    public boolean creer(Client client) {
        String sql = "INSERT INTO client (nom, entreprise, contact, email, telephone, adresse) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getEntreprise());
            pstmt.setString(3, client.getContact());
            pstmt.setString(4, client.getEmail());
            pstmt.setString(5, client.getTelephone());
            pstmt.setString(6, client.getAdresse());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du client : " + e.getMessage());
            return false;
        }
    }
    
    // Récupérer tous les clients
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("entreprise"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getString("date_creation")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients : " + e.getMessage());
        }
        
        return clients;
    }
    
    // Récupérer un client par ID
    public Client getById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("entreprise"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getString("date_creation")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client : " + e.getMessage());
        }
        
        return null;
    }
    
    // Mettre à jour un client
    public boolean modifier(Client client) {
        String sql = "UPDATE client SET nom = ?, entreprise = ?, contact = ?, email = ?, telephone = ?, adresse = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getEntreprise());
            pstmt.setString(3, client.getContact());
            pstmt.setString(4, client.getEmail());
            pstmt.setString(5, client.getTelephone());
            pstmt.setString(6, client.getAdresse());
            pstmt.setInt(7, client.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du client : " + e.getMessage());
            return false;
        }
    }
    
    // Supprimer un client
    public boolean supprimer(int id) {
        String sql = "DELETE FROM client WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client : " + e.getMessage());
            return false;
        }
    }
    
    // Rechercher des clients par nom ou entreprise
    public List<Client> rechercher(String terme) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client WHERE nom LIKE ? OR entreprise LIKE ? ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String termeRecherche = "%" + terme + "%";
            pstmt.setString(1, termeRecherche);
            pstmt.setString(2, termeRecherche);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                clients.add(new Client(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("entreprise"),
                    rs.getString("contact"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getString("date_creation")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de clients : " + e.getMessage());
        }
        
        return clients;
    }
} 