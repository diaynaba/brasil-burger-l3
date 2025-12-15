
package com.brasilburger.views;

import com.brasilburger.entities.Complement;
import com.brasilburger.entities.TypeComplement;
import com.brasilburger.services.interfaces.IComplementService;
import com.brasilburger.utils.ConsoleColor;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ComplementView extends ConsoleView {
    private final IComplementService complementService;
    
    public ComplementView(Scanner scanner, IComplementService complementService) {
        super(scanner);
        this.complementService = complementService;
    }
    
    public void displayMenu() {
        while (true) {
            clearScreen();
            printHeader("GESTION DES COMPLÉMENTS 🥤🍟");
            System.out.println("1. Créer un complément");
            System.out.println("2. Lister tous les compléments");
            System.out.println("3. Lister par type (Boissons/Frites)");
            System.out.println("4. Modifier un complément");
            System.out.println("5. Archiver un complément");
            System.out.println("6. Lister compléments actifs uniquement");
            System.out.println("0. Retour");
            printSeparator();
            
            int choice = validator.readInt("Votre choix: ");
            
            switch (choice) {
                case 1 -> createComplement();
                case 2 -> listAllComplements();
                case 3 -> listByType();
                case 4 -> updateComplement();
                case 5 -> archiveComplement();
                case 6 -> listActiveComplements();
                case 0 -> { return; }
                default -> System.out.println(ConsoleColor.error("Choix invalide!"));
            }
        }
    }
    
    private void createComplement() {
        clearScreen();
        printHeader("CRÉER UN COMPLÉMENT");
        
        try {
            String nom = validator.readNonEmptyString("Nom du complément: ");
            BigDecimal prix = validator.readBigDecimal("Prix (FCFA): ");
            
            // Choix du type
            System.out.println("\nType de complément:");
            System.out.println("1. Boisson");
            System.out.println("2. Frite");
            int typeChoice = validator.readInt("Votre choix: ");
            
            TypeComplement type;
            if (typeChoice == 1) {
                type = TypeComplement.BOISSON;
            } else if (typeChoice == 2) {
                type = TypeComplement.FRITE;
            } else {
                System.out.println(ConsoleColor.error("Type invalide!"));
                pause();
                return;
            }
            
            System.out.print("Chemin de l'image (ou Entrée pour ignorer): ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = imagePath.isEmpty() ? null : new File(imagePath);
            
            if (imageFile != null && !imageFile.exists()) {
                System.out.println(ConsoleColor.warning("Fichier image non trouvé. Création sans image."));
                imageFile = null;
            }
            
            Complement complement = complementService.createComplement(nom, prix, imageFile, type);
            System.out.println(ConsoleColor.success("Complément créé avec succès! ID: " + complement.getId()));
            System.out.println(complement);
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void listAllComplements() {
        clearScreen();
        printHeader("LISTE DES COMPLÉMENTS");
        
        List<Complement> complements = complementService.getAllComplements();
        
        if (complements.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun complément trouvé."));
        } else {
            displayComplementsTable(complements);
        }
        
        pause();
    }
    
    private void listActiveComplements() {
        clearScreen();
        printHeader("COMPLÉMENTS ACTIFS");
        
        List<Complement> complements = complementService.getActiveComplements();
        
        if (complements.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun complément actif."));
        } else {
            displayComplementsTable(complements);
        }
        
        pause();
    }
    
    private void listByType() {
        clearScreen();
        printHeader("FILTRER PAR TYPE");
        
        System.out.println("1. Boissons");
        System.out.println("2. Frites");
        int choice = validator.readInt("Votre choix: ");
        
        TypeComplement type = (choice == 1) ? TypeComplement.BOISSON : TypeComplement.FRITE;
        List<Complement> complements = complementService.getComplementsByType(type);
        
        clearScreen();
        printHeader("LISTE DES " + type.getLibelle().toUpperCase() + "S");
        
        if (complements.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun " + type.getLibelle() + " trouvé."));
        } else {
            displayComplementsTable(complements);
        }
        
        pause();
    }
    
    private void updateComplement() {
        clearScreen();
        printHeader("MODIFIER UN COMPLÉMENT");
        
        listActiveComplements();
        
        try {
            int id = validator.readInt("\nID du complément à modifier: ");
            Complement complement = complementService.getComplementById(id);
            
            System.out.println("\nComplément actuel: " + complement);
            System.out.println(ConsoleColor.info("Laissez vide pour conserver la valeur actuelle"));
            
            System.out.print("Nouveau nom [" + complement.getNom() + "]: ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) nom = complement.getNom();
            
            System.out.print("Nouveau prix [" + complement.getPrix() + "]: ");
            String prixStr = scanner.nextLine().trim();
            BigDecimal prix = prixStr.isEmpty() ? complement.getPrix() : new BigDecimal(prixStr);
            
            System.out.print("Nouveau chemin image (Entrée pour garder): ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = imagePath.isEmpty() ? null : new File(imagePath);
            
            Complement updated = complementService.updateComplement(id, nom, prix, imageFile);
            System.out.println(ConsoleColor.success("Complément modifié avec succès!"));
            System.out.println(updated);
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void archiveComplement() {
        clearScreen();
        printHeader("ARCHIVER UN COMPLÉMENT");
        
        listActiveComplements();
        
        try {
            int id = validator.readInt("\nID du complément à archiver: ");
            Complement complement = complementService.getComplementById(id);
            
            System.out.println("\nComplément à archiver: " + complement);
            
            if (validator.readConfirmation("Confirmer l'archivage ?")) {
                complementService.archiveComplement(id);
                System.out.println(ConsoleColor.success("Complément archivé avec succès!"));
            } else {
                System.out.println(ConsoleColor.info("Archivage annulé."));
            }
            
        } catch (IllegalStateException e) {
            System.out.println(ConsoleColor.error("Impossible d'archiver: " + e.getMessage()));
            System.out.println(ConsoleColor.warning("Ce complément est utilisé dans des menus actifs."));
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void displayComplementsTable(List<Complement> complements) {
        System.out.printf("%-5s %-25s %-12s %-15s %-10s%n", 
            "ID", "NOM", "TYPE", "PRIX (FCFA)", "STATUT");
        printSeparator();
        
        for (Complement c : complements) {
            String statut = c.isEstArchive() ? 
                ConsoleColor.warning("Archivé") : 
                ConsoleColor.success("Actif");
            
            String typeIcon = c.getType() == TypeComplement.BOISSON ? "🥤" : "🍟";
            
            System.out.printf("%-5d %-25s %-12s %-15.2f %s%n", 
                c.getId(), 
                c.getNom(), 
                typeIcon + " " + c.getType().getLibelle(), 
                c.getPrix(), 
                statut);
        }
        
        System.out.println("\nTotal: " + complements.size() + " complément(s)");
    }
}