
package com.brasilburger.repositories.impl;

import com.brasilburger.config.DatabaseConfig;
import com.brasilburger.entities.*;
import com.brasilburger.repositories.interfaces.IMenuRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryImpl implements IMenuRepository {
    private final Connection connection;
    
    public MenuRepositoryImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }
    
    @Override
    public Menu save(Menu menu) {
        String sql = menu.getId() == null ?
            "INSERT INTO Menu (nom, image, burger_id, boisson_id, frite_id, est_archive) VALUES (?, ?, ?, ?, ?, ?) RETURNING id" :
            "UPDATE Menu SET nom = ?, image = ?, burger_id = ?, boisson_id = ?, frite_id = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (menu.getId() == null) {
                stmt.setString(1, menu.getNom());
                stmt.setString(2, menu.getImage());
                stmt.setInt(3, menu.getBurgerId());
                stmt.setInt(4, menu.getBoissonId());
                stmt.setInt(5, menu.getFriteId());
                stmt.setBoolean(6, menu.isEstArchive());
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    menu.setId(rs.getInt("id"));
                }
            } else {
                stmt.setString(1, menu.getNom());
                stmt.setString(2, menu.getImage());
                stmt.setInt(3, menu.getBurgerId());
                stmt.setInt(4, menu.getBoissonId());
                stmt.setInt(5, menu.getFriteId());
                stmt.setInt(6, menu.getId());
                stmt.executeUpdate();
            }
            return menu;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du menu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Menu> findById(Integer id) {
        String sql = "SELECT * FROM Menu WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToMenu(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du menu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Menu findByIdWithDetails(Integer id) {
        String sql = """
            SELECT m.*, 
                   b.id as burger_id, b.nom as burger_nom, b.prix as burger_prix, b.image as burger_image,
                   c1.id as boisson_id, c1.nom as boisson_nom, c1.prix as boisson_prix, c1.type as boisson_type,
                   c2.id as frite_id, c2.nom as frite_nom, c2.prix as frite_prix, c2.type as frite_type
            FROM Menu m
            JOIN Burger b ON m.burger_id = b.id
            JOIN Complement c1 ON m.boisson_id = c1.id
            JOIN Complement c2 ON m.frite_id = c2.id
            WHERE m.id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Menu menu = mapResultSetToMenu(rs);
                
                Burger burger = new Burger();
                burger.setId(rs.getInt("burger_id"));
                burger.setNom(rs.getString("burger_nom"));
                burger.setPrix(rs.getBigDecimal("burger_prix"));
                burger.setImage(rs.getString("burger_image"));
                menu.setBurger(burger);
                
                Complement boisson = new Complement();
                boisson.setId(rs.getInt("boisson_id"));
                boisson.setNom(rs.getString("boisson_nom"));
                boisson.setPrix(rs.getBigDecimal("boisson_prix"));
                boisson.setType(TypeComplement.valueOf(rs.getString("boisson_type")));
                menu.setBoisson(boisson);
                
                Complement frite = new Complement();
                frite.setId(rs.getInt("frite_id"));
                frite.setNom(rs.getString("frite_nom"));
                frite.setPrix(rs.getBigDecimal("frite_prix"));
                frite.setType(TypeComplement.valueOf(rs.getString("frite_type")));
                menu.setFrite(frite);
                
                return menu;
            }
            throw new RuntimeException("Menu non trouvé");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du menu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Menu> findAll() {
        String sql = "SELECT * FROM Menu ORDER BY date_creation DESC";
        return executeQuery(sql);
    }
    
    @Override
    public List<Menu> findAllActive() {
        String sql = "SELECT * FROM Menu WHERE est_archive = false ORDER BY date_creation DESC";
        return executeQuery(sql);
    }
    
    @Override
    public void archive(Integer id) {
        String sql = "UPDATE Menu SET est_archive = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'archivage du menu: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Menu WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du menu: " + e.getMessage(), e);
        }
    }
    
    private List<Menu> executeQuery(String sql) {
        List<Menu> menus = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
            return menus;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la requête: " + e.getMessage(), e);
        }
    }
    
    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("id"));
        menu.setNom(rs.getString("nom"));
        menu.setImage(rs.getString("image"));
        menu.setBurgerId(rs.getInt("burger_id"));
        menu.setBoissonId(rs.getInt("boisson_id"));
        menu.setFriteId(rs.getInt("frite_id"));
        menu.setEstArchive(rs.getBoolean("est_archive"));
        menu.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return menu;
    }
}