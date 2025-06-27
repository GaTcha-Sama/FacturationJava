package com.facturation.dao;

import com.facturation.model.Utilisateur;
import com.facturation.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {
    
    // Authentifier un utilisateur
    public Utilisateur authentifier(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, motDePasse);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe"),
                    rs.getString("date_creation")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
        }
        
        return null;
    }
    
    // Créer un nouvel utilisateur
    public boolean creer(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (nom, email, mot_de_passe) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getEmail());
            pstmt.setString(3, utilisateur.getMotDePasse());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }
    
    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY nom";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurs.add(new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getString("mot_de_passe"),
                    rs.getString("date_creation")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        
        return utilisateurs;
    }
    
    // Vérifier si un email existe déjà
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
        }
        
        return false;
    }
} 