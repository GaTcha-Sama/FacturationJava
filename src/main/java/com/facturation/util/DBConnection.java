package com.facturation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:facturation.db";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try {
            // Créer les tables si elles n'existent pas
            createTables();
            System.out.println("Base de données initialisée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
        }
    }

    private static void createTables() throws SQLException {
        String[] createTableQueries = {
            // Table utilisateur
            "CREATE TABLE IF NOT EXISTS utilisateur (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nom TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "mot_de_passe TEXT NOT NULL, " +
            "date_creation DATETIME DEFAULT CURRENT_TIMESTAMP)",

            // Table client
            "CREATE TABLE IF NOT EXISTS client (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nom TEXT NOT NULL, " +
            "entreprise TEXT, " +
            "contact TEXT, " +
            "email TEXT, " +
            "telephone TEXT, " +
            "adresse TEXT, " +
            "date_creation DATETIME DEFAULT CURRENT_TIMESTAMP)",

            // Table prestation
            "CREATE TABLE IF NOT EXISTS prestation (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "type TEXT CHECK(type IN ('formation', 'consultation')) NOT NULL, " +
            "date TEXT NOT NULL, " +
            "heure_debut TEXT, " +
            "heure_fin TEXT, " +
            "description TEXT, " +
            "tjm REAL, " +
            "module TEXT, " +
            "classe TEXT, " +
            "entreprise TEXT, " +
            "client_id INTEGER NOT NULL, " +
            "montant_calcule REAL, " +
            "date_creation DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE)",

            // Table facture
            "CREATE TABLE IF NOT EXISTS facture (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "numero_facture TEXT UNIQUE NOT NULL, " +
            "mois INTEGER NOT NULL, " +
            "annee INTEGER NOT NULL, " +
            "montant_total REAL NOT NULL, " +
            "statut TEXT DEFAULT 'en_attente' CHECK(statut IN ('en_attente', 'payee', 'annulee')), " +
            "fichier_pdf TEXT, " +
            "client_id INTEGER NOT NULL, " +
            "date_creation DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "date_paiement DATETIME, " +
            "FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE)"
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            for (String query : createTableQueries) {
                stmt.execute(query);
            }

            // Insérer un utilisateur par défaut si la table est vide
            stmt.execute("INSERT OR IGNORE INTO utilisateur (nom, email, mot_de_passe) " +
                        "VALUES ('Admin', 'admin@example.com', 'admin123')");

            // Insérer quelques clients de test
            stmt.execute("INSERT OR IGNORE INTO client (nom, entreprise, contact, email) VALUES " +
                        "('Jean Dupont', 'Entreprise ABC', 'Jean Dupont', 'jean.dupont@abc.com'), " +
                        "('Marie Martin', 'Société XYZ', 'Marie Martin', 'marie.martin@xyz.com')");

        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }
} 