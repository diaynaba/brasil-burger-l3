
package com.brasilburger.repositories.impl;

import com.brasilburger.config.DatabaseConfig;
import com.brasilburger.entities.Complement;
import com.brasilburger.entities.TypeComplement;
import com.brasilburger.repositories.interfaces.IComplementRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplementRepositoryImpl implements IComplementRepository {
    private final Connection connection;
    
    public ComplementRepositoryImpl() {
        this.connection = DatabaseConfig.getInstance().getConnection();
    }
    
    @Override
    public Complement save(Complement complement) {
        String sql = complement.getId() == null ?
            "INSERT INTO Complement (nom, prix, image, type, est_archive) VALUES (?, ?, ?, ?::type_complement, ?) RETURNING id" :
            "UPDATE Complement SET nom = ?, prix = ?, image = ?, type = ?::type_complement WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (complement.getId() == null) {
                stmt.setString(1, complement.getNom());
                stmt.setBigDecimal(2, complement.getPrix());
                stmt.setString(3, complement.getImage());
                stmt.setString(4, complement.getType().name());
                stmt.setBoolean(5, complement.isEstArchive());
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    complement.setId(rs.getInt("id"));
                }
            } else {
                stmt.setString(1, complement.getNom());
                stmt.setBigDecimal(2, complement.getPrix());
                stmt.setString(3, complement.getImage());
                stmt.setString(4, complement.getType().name());
                stmt.setInt(5, complement.getId());
                stmt.executeUpdate();
            }
            return complement;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du complément: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Complement> findById(Integer id) {
        String sql = "SELECT * FROM Complement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToComplement(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du complément: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Complement> findAll() {
        String sql = "SELECT * FROM Complement ORDER BY type, nom";
        return executeQuery(sql);
    }
    
    @Override
    public List<Complement> findAllActive() {
        String sql = "SELECT * FROM Complement WHERE est_archive = false ORDER BY type, nom";
        return executeQuery(sql);
    }
    
    @Override
    public List<Complement> findByType(TypeComplement type) {
        String sql = "SELECT * FROM Complement WHERE type = ?::type_complement AND est_archive = false ORDER BY nom";
        List<Complement> complements = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
            return complements;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par type: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void archive(Integer id) {
        if (isUsedInActiveMenus(id)) {
            throw new IllegalStateException("Ce complément est utilisé dans des menus actifs");
        }
        String sql = "UPDATE Complement SET est_archive = true WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'archivage: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isUsedInActiveMenus(Integer complementId) {
        String sql = "SELECT COUNT(*) FROM Menu WHERE (boisson_id = ? OR frite_id = ?) AND est_archive = false";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, complementId);
            stmt.setInt(2, complementId);
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
        String sql = "DELETE FROM Complement WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression: " + e.getMessage(), e);
        }
    }
    
    private List<Complement> executeQuery(String sql) {
        List<Complement> complements = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                complements.add(mapResultSetToComplement(rs));
            }
            return complements;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la requête: " + e.getMessage(), e);
        }
    }
    
    private Complement mapResultSetToComplement(ResultSet rs) throws SQLException {
        Complement complement = new Complement();
        complement.setId(rs.getInt("id"));
        complement.setNom(rs.getString("nom"));
        complement.setPrix(rs.getBigDecimal("prix"));
        complement.setImage(rs.getString("image"));
        complement.setType(TypeComplement.valueOf(rs.getString("type")));
        complement.setEstArchive(rs.getBoolean("est_archive"));
        complement.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        return complement;
    }
}
