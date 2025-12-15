

package com.brasilburger.services.impl;

import com.brasilburger.entities.Burger;
import com.brasilburger.repositories.interfaces.IBurgerRepository;
import com.brasilburger.services.interfaces.IBurgerService;
import com.brasilburger.services.interfaces.IImageService;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class BurgerServiceImpl implements IBurgerService {
    private final IBurgerRepository burgerRepository;
    private final IImageService imageService;
    
    // Dependency Inversion Principle
    public BurgerServiceImpl(IBurgerRepository burgerRepository, IImageService imageService) {
        this.burgerRepository = burgerRepository;
        this.imageService = imageService;
    }
    
    @Override
    public Burger createBurger(String nom, BigDecimal prix, File imageFile) {
        validateBurgerData(nom, prix);
        
        String imageUrl = null;
        if (imageFile != null && imageFile.exists()) {
            imageUrl = imageService.uploadImage(imageFile, "burgers");
        }
        
        Burger burger = new Burger(nom, prix, imageUrl);
        return burgerRepository.save(burger);
    }
    
    @Override
    public Burger updateBurger(Integer id, String nom, BigDecimal prix, File imageFile) {
        validateBurgerData(nom, prix);
        
        Burger burger = burgerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Burger non trouvé"));
        
        burger.setNom(nom);
        burger.setPrix(prix);
        
        if (imageFile != null && imageFile.exists()) {
           
            if (burger.getImage() != null) {
                imageService.deleteImage(burger.getImage());
            }
            
            String imageUrl = imageService.uploadImage(imageFile, "burgers");
            burger.setImage(imageUrl);
        }
        
        return burgerRepository.save(burger);
    }
    
    @Override
    public void archiveBurger(Integer id) {
        burgerRepository.archive(id);
    }
    
    @Override
    public Burger getBurgerById(Integer id) {
        return burgerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Burger non trouvé"));
    }
    
    @Override
    public List<Burger> getAllBurgers() {
        return burgerRepository.findAll();
    }
    
    @Override
    public List<Burger> getActiveBurgers() {
        return burgerRepository.findAllActive();
    }
    
    private void validateBurgerData(String nom, BigDecimal prix) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du burger est obligatoire");
        }
        if (prix == null || prix.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }
    }
}

