
package com.brasilburger.entities;

import java.math.BigDecimal;

public class Complement extends BaseEntity {
    private String nom;
    private BigDecimal prix;
    private String image;
    private TypeComplement type;
    private boolean estArchive;
    
    public Complement() {
        super();
        this.estArchive = false;
    }
    
    public Complement(String nom, BigDecimal prix, String image, TypeComplement type) {
        this();
        this.nom = nom;
        this.prix = prix;
        this.image = image;
        this.type = type;
    }
    
    // Getters et Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public TypeComplement getType() { return type; }
    public void setType(TypeComplement type) { this.type = type; }
    
    public boolean isEstArchive() { return estArchive; }
    public void setEstArchive(boolean estArchive) { this.estArchive = estArchive; }
    
    @Override
    public String toString() {
        return String.format("Complement[id=%d, nom='%s', type=%s, prix=%.2f FCFA, archive=%s]",
            id, nom, type.getLibelle(), prix, estArchive ? "Oui" : "Non");
    }
}