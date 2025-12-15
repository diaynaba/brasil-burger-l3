
package com.brasilburger.services.impl;

import com.brasilburger.entities.Complement;
import com.brasilburger.entities.TypeComplement;
import com.brasilburger.repositories.interfaces.IComplementRepository;
import com.brasilburger.services.interfaces.IComplementService;
import com.brasilburger.services.interfaces.IImageService;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class ComplementServiceImpl implements IComplementService {
    private final IComplementRepository complementRepository;
    private final IImageService imageService;
    
    public ComplementServiceImpl(IComplementRepository complementRepository, IImageService imageService) {
        this.complementRepository = complementRepository;
        this.imageService = imageService;
    }
    
    @Override
    public Complement createComplement(String nom, BigDecimal prix, File imageFile, TypeComplement type) {
        validateComplementData(nom, prix, type);
        
        String imageUrl = null;
        if (imageFile != null && imageFile.exists()) {
            imageUrl = imageService.uploadImage(imageFile, "complements");
        }
        
        Complement complement = new Complement(nom, prix, imageUrl, type);
        return complementRepository.save(complement);
    }
    
    @Override
    public Complement updateComplement(Integer id, String nom, BigDecimal prix, File imageFile) {
        validateComplementData(nom, prix, null);
        
        Complement complement = complementRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Complément non trouvé"));
        
        complement.setNom(nom);
        complement.setPrix(prix);
        
        if (imageFile != null && imageFile.exists()) {
            if (complement.getImage() != null) {
                imageService.deleteImage(complement.getImage());
            }
            String imageUrl = imageService.uploadImage(imageFile, "complements");
            complement.setImage(imageUrl);
        }
        
        return complementRepository.save(complement);
    }
    
    @Override
    public void archiveComplement(Integer id) {
        complementRepository.archive(id);
    }
    
    @Override
    public Complement getComplementById(Integer id) {
        return complementRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Complément non trouvé"));
    }
    
    @Override
    public List<Complement> getAllComplements() {
        return complementRepository.findAll();
    }
    
    @Override
    public List<Complement> getActiveComplements() {
        return complementRepository.findAllActive();
    }
    
    @Override
    public List<Complement> getComplementsByType(TypeComplement type) {
        return complementRepository.findByType(type);
    }
    
    private void validateComplementData(String nom, BigDecimal prix, TypeComplement type) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (prix == null || prix.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }
        
        if (type == null && prix != null) {
            return; 
        }
        if (type == null) {
            throw new IllegalArgumentException("Le type est obligatoire");
        }
    }
}