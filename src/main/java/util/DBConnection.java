package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
        try (Connection conn = getConnection()) {
            // Lire et exécuter le script SQL
            String sqlScript = readSQLFile();
            String[] statements = sqlScript.split(";");
            
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(statement);
                    }
                }
            }
            
            // Nettoyer les doublons après l'initialisation
            nettoyerDoublonsClients();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation de la base de données : " + e.getMessage());
        }
    }

    private static void nettoyerDoublonsClients() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Supprimer les doublons en gardant le plus ancien (ID le plus petit)
            String sql = "DELETE FROM client WHERE id NOT IN (" +
                         "SELECT MIN(id) FROM client GROUP BY nom, entreprise, email)";
            
            int rowsAffected = stmt.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("Nettoyage automatique : " + rowsAffected + " doublons supprimés");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du nettoyage automatique des doublons : " + e.getMessage());
        }
    }

    private static String readSQLFile() {
        // Implementation of readSQLFile method
        return ""; // Placeholder return, actual implementation needed
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