package com.brasilburger.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static DatabaseConfig instance;
    private Connection connection;
    
    private DatabaseConfig() {
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
            
            this.connection = DriverManager.getConnection(url, props);
            System.out.println("✅ Connexion à Neon PostgreSQL réussie!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                instance = new DatabaseConfig();
            }
        } catch (SQLException e) {
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
}