
package com.brasilburger.repositories.impl;

import com.brasilburger.config.DatabaseConfig;
import com.brasilburger.entities.Burger;
import com.brasilburger.repositories.interfaces.IBurgerRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BurgerRepositoryImpl implements IBurgerRepository {
    private final Connection connection;
    
    public BurgerRepositoryImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }
    
    @Override
    public Burger save(Burger burger) {
        String sql = burger.getId() == null ? 
            "INSERT INTO Burger (nom, prix, image, est_archive) VALUES (?, ?, ?, ?) RETURNING id" :
            "UPDATE Burger SET nom = ?, prix = ?, image = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (burger.getId() == null) {
                // INSERT
                stmt.setString(1, burger.getNom());
                stmt.setBigDecimal(2, burger.getPrix());
                stmt.setString(3, burger.getImage());
                stmt.setBoolean(4, burger.isEstArchive());
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    burger.setId(rs.getInt("id"));
                }
            } else {
                // UPDATE
                stmt.setString(1, burger.getNom());
                stmt.setBigDecimal(2, burger.getPrix());
                stmt.setString(3, burger.getImage());
                stmt.setInt(4, burger.getId());
                stmt.executeUpdate();
            }
            return burger;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du burger: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Burger> findById(Integer id) {
        String sql = "SELECT * FROM Burger WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToBurger(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du burger: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Burger> findAll() {
        String sql = "SELECT * FROM Burger ORDER BY date_creation DESC";
        List<Burger> burgers = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
            return burgers;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des burgers: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Burger> findAllActive() {
        String sql = "SELECT * FROM Burger WHERE est_archive = false ORDER BY date_creation DESC";
        List<Burger> burgers = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                burgers.add(mapResultSetToBurger(rs));
            }
            return burgers;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des burgers actifs: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void archive(Integer id) {
        if (isUsedInActiveMenus(id)) {
            throw new IllegalStateException("Ce burger est utilisé dans des menus actifs");
        }
        String sql = "UPDATE Burger SET est_archive = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'archivage du burger: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isUsedInActiveMenus(Integer burgerId) {
        String sql = "SELECT COUNT(*) FROM Menu WHERE burger_id = ? AND est_archive = false";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, burgerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Burger WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du burger: " + e.getMessage(), e);
        }
    }
    
    private Burger mapResultSetToBurger(ResultSet rs) throws SQLException {
        Burger burger = new Burger();
        burger.setId(rs.getInt("id"));
        burger.setNom(rs.getString("nom"));
        burger.setPrix(rs.getBigDecimal("prix"));
        burger.setImage(rs.getString("image"));
        burger.setEstArchive(rs.getBoolean("est_archive"));
        burger.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return burger;
    }
}
