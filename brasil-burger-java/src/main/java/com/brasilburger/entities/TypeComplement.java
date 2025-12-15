
package com.brasilburger.entities;

public enum TypeComplement {
    BOISSON("Boisson"),
    FRITE("Frite");
    
    private final String libelle;
    
    TypeComplement(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() { return libelle; }
    
    public static TypeComplement fromString(String text) {
        for (TypeComplement type : TypeComplement.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type invalide: " + text);
    }
}