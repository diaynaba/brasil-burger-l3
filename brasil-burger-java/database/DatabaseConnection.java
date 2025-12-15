
package com.brasilburger.database;

import com.brasilburger.config.EnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


@Deprecated
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            String host = EnvConfig.get("DB_HOST");
            String port = EnvConfig.get("DB_PORT", "5432");
            String dbName = EnvConfig.get("DB_NAME");
            String user = EnvConfig.get("DB_USER");
            String password = EnvConfig.get("DB_PASSWORD");
            String sslMode = EnvConfig.get("DB_SSL_MODE", "require");
            
            String url = String.format(
                "jdbc:postgresql://%s:%s/%s?sslmode=%s",
                host, port, dbName, sslMode
            );
            
            Properties props = new Properties();
            props.setProperty("user", user);
            props.setProperty("password", password);
            
            Class.forName("org.postgresql.Driver");
            
            this.connection = DriverManager.getConnection(url, props);
            System.out.println("✅ Connexion à la base de données établie!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver PostgreSQL non trouvé: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données: " + e.getMessage());
            System.err.println("   Vérifiez votre fichier .env");
            throw new RuntimeException(e);
        }
    }
    
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
    
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la vérification de la connexion: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return connection;
    }
    
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la fermeture: " + e.getMessage());
        }
    }
    
   
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}