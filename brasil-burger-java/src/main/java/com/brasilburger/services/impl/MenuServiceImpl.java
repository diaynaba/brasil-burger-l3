
package com.brasilburger.services.impl;

import com.brasilburger.entities.*;
import com.brasilburger.repositories.interfaces.IMenuRepository;
import com.brasilburger.services.interfaces.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class MenuServiceImpl implements IMenuService {
    private final IMenuRepository menuRepository;
    private final IBurgerService burgerService;
    private final IComplementService complementService;
    private final IImageService imageService;
    
    public MenuServiceImpl(IMenuRepository menuRepository, 
                          IBurgerService burgerService,
                          IComplementService complementService,
                          IImageService imageService) {
        this.menuRepository = menuRepository;
        this.burgerService = burgerService;
        this.complementService = complementService;
        this.imageService = imageService;
    }
    
    @Override
    public Menu createMenu(String nom, File imageFile, Integer burgerId, 
                          Integer boissonId, Integer friteId) {
        validateMenuData(nom, burgerId, boissonId, friteId);
        
        String imageUrl = null;
        if (imageFile != null && imageFile.exists()) {
            imageUrl = imageService.uploadImage(imageFile, "menus");
        }
        
        Menu menu = new Menu(nom, imageUrl, burgerId, boissonId, friteId);
        return menuRepository.save(menu);
    }
    
    @Override
    public Menu updateMenu(Integer id, String nom, File imageFile, 
                          Integer burgerId, Integer boissonId, Integer friteId) {
        validateMenuData(nom, burgerId, boissonId, friteId);
        
        Menu menu = menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu non trouvé"));
        
        menu.setNom(nom);
        menu.setBurgerId(burgerId);
        menu.setBoissonId(boissonId);
        menu.setFriteId(friteId);
        
        if (imageFile != null && imageFile.exists()) {
            if (menu.getImage() != null) {
                imageService.deleteImage(menu.getImage());
            }
            String imageUrl = imageService.uploadImage(imageFile, "menus");
            menu.setImage(imageUrl);
        }
        
        return menuRepository.save(menu);
    }
    
    @Override
    public void archiveMenu(Integer id) {
        menuRepository.archive(id);
    }
    
    @Override
    public Menu getMenuById(Integer id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu non trouvé"));
    }
    
    @Override
    public Menu getMenuWithDetails(Integer id) {
        return menuRepository.findByIdWithDetails(id);
    }
    
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    
    @Override
    public List<Menu> getActiveMenus() {
        return menuRepository.findAllActive();
    }
    
    @Override
    public BigDecimal calculateMenuPrice(Integer burgerId, Integer boissonId, Integer friteId) {
        Burger burger = burgerService.getBurgerById(burgerId);
        Complement boisson = complementService.getComplementById(boissonId);
        Complement frite = complementService.getComplementById(friteId);
        
        return burger.getPrix()
            .add(boisson.getPrix())
            .add(frite.getPrix());
    }
    
    private void validateMenuData(String nom, Integer burgerId, Integer boissonId, Integer friteId) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du menu est obligatoire");
        }
        if (burgerId == null) {
            throw new IllegalArgumentException("Un burger doit être sélectionné");
        }
        if (boissonId == null) {
            throw new IllegalArgumentException("Une boisson doit être sélectionnée");
        }
        if (friteId == null) {
            throw new IllegalArgumentException("Des frites doivent être sélectionnées");
        }
        
        // Vérifier que les éléments existent et sont actifs
        burgerService.getBurgerById(burgerId);
        
        Complement boisson = complementService.getComplementById(boissonId);
        if (boisson.getType() != TypeComplement.BOISSON) {
            throw new IllegalArgumentException("L'ID boisson doit correspondre à une boisson");
        }
        
        Complement frite = complementService.getComplementById(friteId);
        if (frite.getType() != TypeComplement.FRITE) {
            throw new IllegalArgumentException("L'ID frite doit correspondre à des frites");
        }
    }
}
