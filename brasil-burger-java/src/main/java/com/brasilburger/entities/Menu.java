
package com.brasilburger.entities;

import java.math.BigDecimal;

public class Menu extends BaseEntity {
    private String nom;
    private String image;
    private boolean estArchive;
    private Integer burgerId;
    private Integer boissonId;
    private Integer friteId;
    
   
    private Burger burger;
    private Complement boisson;
    private Complement frite;
    
    public Menu() {
        super();
        this.estArchive = false;
    }
    
    public Menu(String nom, String image, Integer burgerId, Integer boissonId, Integer friteId) {
        this();
        this.nom = nom;
        this.image = image;
        this.burgerId = burgerId;
        this.boissonId = boissonId;
        this.friteId = friteId;
    }
    
    public BigDecimal calculerPrixTotal() {
        BigDecimal total = BigDecimal.ZERO;
        if (burger != null) total = total.add(burger.getPrix());
        if (boisson != null) total = total.add(boisson.getPrix());
        if (frite != null) total = total.add(frite.getPrix());
        return total;
    }
    
    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public boolean isEstArchive() { return estArchive; }
    public void setEstArchive(boolean estArchive) { this.estArchive = estArchive; }
    
    public Integer getBurgerId() { return burgerId; }
    public void setBurgerId(Integer burgerId) { this.burgerId = burgerId; }
    
    public Integer getBoissonId() { return boissonId; }
    public void setBoissonId(Integer boissonId) { this.boissonId = boissonId; }
    
    public Integer getFriteId() { return friteId; }
    public void setFriteId(Integer friteId) { this.friteId = friteId; }
    
    public Burger getBurger() { return burger; }
    public void setBurger(Burger burger) { this.burger = burger; }
    
    public Complement getBoisson() { return boisson; }
    public void setBoisson(Complement boisson) { this.boisson = boisson; }
    
    public Complement getFrite() { return frite; }
    public void setFrite(Complement frite) { this.frite = frite; }
    
    @Override
    public String toString() {
        return String.format("Menu[id=%d, nom='%s', prix=%.2f FCFA, archive=%s]",
            id, nom, calculerPrixTotal(), estArchive ? "Oui" : "Non");
    }
}