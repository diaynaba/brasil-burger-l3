
package com.brasilburger.entities;

import java.math.BigDecimal;

public class Burger extends BaseEntity {
    private String nom;
    private BigDecimal prix;
    private String image;
    private boolean estArchive;
    
    public Burger() {
        super();
        this.estArchive = false;
    }
    
    public Burger(String nom, BigDecimal prix, String image) {
        this();
        this.nom = nom;
        this.prix = prix;
        this.image = image;
    }
    
    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public boolean isEstArchive() { return estArchive; }
    public void setEstArchive(boolean estArchive) { this.estArchive = estArchive; }
    
    @Override
    public String toString() {
        return String.format("Burger[id=%d, nom='%s', prix=%.2f FCFA, archive=%s]",
            id, nom, prix, estArchive ? "Oui" : "Non");
    }
}
