package dao;

import model.Client;
import util.DBConnection;
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
    
    // Récupérer tous les clients (version améliorée pour éviter les doublons)
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT DISTINCT id, nom, entreprise, contact, email, telephone, adresse, date_creation " +
                     "FROM client ORDER BY nom";
        
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
    
    // Nouvelle méthode pour nettoyer les doublons
    public boolean nettoyerDoublons() {
        String sql = "DELETE FROM client WHERE id NOT IN (" +
                     "SELECT MIN(id) FROM client GROUP BY nom, entreprise, email)";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Doublons supprimés : " + rowsAffected + " lignes");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du nettoyage des doublons : " + e.getMessage());
            return false;
        }
    }
    
    // Nouvelle méthode pour vérifier s'il y a des doublons
    public boolean hasDoublons() {
        String sql = "SELECT COUNT(*) as total, COUNT(DISTINCT nom || '|' || COALESCE(entreprise, '') || '|' || COALESCE(email, '')) as unique_count " +
                     "FROM client";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int total = rs.getInt("total");
                int uniqueCount = rs.getInt("unique_count");
                return total > uniqueCount;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification des doublons : " + e.getMessage());
        }
        
        return false;
    }
} 