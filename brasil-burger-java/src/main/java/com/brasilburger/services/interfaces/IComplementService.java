
package com.brasilburger.services.interfaces;

import com.brasilburger.entities.Complement;
import com.brasilburger.entities.TypeComplement;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;


public interface IComplementService {
    
    /**
     * Crée un nouveau complément
     * @param nom Nom du complément
     * @param prix Prix du complément
     * @param imageFile Fichier image (optionnel)
     * @param type Type de complément (BOISSON ou FRITE)
     * @return Le complément créé
     */
    Complement createComplement(String nom, BigDecimal prix, File imageFile, TypeComplement type);
    
    /**
     * Met à jour un complément existant
     * @param id ID du complément
     * @param nom Nouveau nom
     * @param prix Nouveau prix
     * @param imageFile Nouveau fichier image (optionnel)
     * @return Le complément mis à jour
     */
    Complement updateComplement(Integer id, String nom, BigDecimal prix, File imageFile);
    
    /**
     * Archive un complément
     * @param id ID du complément à archiver
     * @throws IllegalStateException si le complément est utilisé dans des menus actifs
     */
    void archiveComplement(Integer id);
    
    /**
     * Récupère un complément par son ID
     * @param id ID du complément
     * @return Le complément trouvé
     * @throws IllegalArgumentException si le complément n'existe pas
     */
    Complement getComplementById(Integer id);
    
    /**
     * Récupère tous les compléments (actifs et archivés)
     * @return Liste de tous les compléments
     */
    List<Complement> getAllComplements();
    
    /**
     * Récupère uniquement les compléments actifs
     * @return Liste des compléments actifs
     */
    List<Complement> getActiveComplements();
    
    /**
     * Récupère les compléments par type
     * @param type Type de complément (BOISSON ou FRITE)
     * @return Liste des compléments du type spécifié
     */
    List<Complement> getComplementsByType(TypeComplement type);
}

