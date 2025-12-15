
package com.brasilburger.views;

import com.brasilburger.entities.Burger;
import com.brasilburger.services.interfaces.IBurgerService;
import com.brasilburger.utils.ConsoleColor;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BurgerView extends ConsoleView {
    private final IBurgerService burgerService;
    
    public BurgerView(Scanner scanner, IBurgerService burgerService) {
        super(scanner);
        this.burgerService = burgerService;
    }
    
    public void displayMenu() {
        while (true) {
            clearScreen();
            printHeader("GESTION DES BURGERS 🍔");
            System.out.println("1. Créer un burger");
            System.out.println("2. Lister les burgers");
            System.out.println("3. Modifier un burger");
            System.out.println("4. Archiver un burger");
            System.out.println("5. Lister burgers actifs uniquement");
            System.out.println("0. Retour");
            printSeparator();
            
            int choice = validator.readInt("Votre choix: ");
            
            switch (choice) {
                case 1 -> createBurger();
                case 2 -> listAllBurgers();
                case 3 -> updateBurger();
                case 4 -> archiveBurger();
                case 5 -> listActiveBurgers();
                case 0 -> { return; }
                default -> System.out.println(ConsoleColor.error("Choix invalide!"));
            }
        }
    }
    
    private void createBurger() {
        clearScreen();
        printHeader("CRÉER UN BURGER");
        
        try {
            String nom = validator.readNonEmptyString("Nom du burger: ");
            BigDecimal prix = validator.readBigDecimal("Prix (FCFA): ");
            
            System.out.print("Chemin de l'image (ou Entrée pour ignorer): ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = imagePath.isEmpty() ? null : new File(imagePath);
            
            if (imageFile != null && !imageFile.exists()) {
                System.out.println(ConsoleColor.warning("Fichier image non trouvé. Création sans image."));
                imageFile = null;
            }
            
            Burger burger = burgerService.createBurger(nom, prix, imageFile);
            System.out.println(ConsoleColor.success("Burger créé avec succès! ID: " + burger.getId()));
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void listAllBurgers() {
        clearScreen();
        printHeader("LISTE DES BURGERS");
        
        List<Burger> burgers = burgerService.getAllBurgers();
        
        if (burgers.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun burger trouvé."));
        } else {
            System.out.printf("%-5s %-25s %-15s %-10s%n", "ID", "NOM", "PRIX (FCFA)", "STATUT");
            printSeparator();
            for (Burger b : burgers) {
                String statut = b.isEstArchive() ? 
                    ConsoleColor.warning("Archivé") : 
                    ConsoleColor.success("Actif");
                System.out.printf("%-5d %-25s %-15.2f %s%n", 
                    b.getId(), b.getNom(), b.getPrix(), statut);
            }
            System.out.println("\nTotal: " + burgers.size() + " burger(s)");
        }
        
        pause();
    }
    
    private void listActiveBurgers() {
        clearScreen();
        printHeader("BURGERS ACTIFS");
        
        List<Burger> burgers = burgerService.getActiveBurgers();
        
        if (burgers.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun burger actif."));
        } else {
            System.out.printf("%-5s %-25s %-15s%n", "ID", "NOM", "PRIX (FCFA)");
            printSeparator();
            for (Burger b : burgers) {
                System.out.printf("%-5d %-25s %-15.2f%n", 
                    b.getId(), b.getNom(), b.getPrix());
            }
            System.out.println("\nTotal: " + burgers.size() + " burger(s) actif(s)");
        }
        
        pause();
    }
    
    private void updateBurger() {
        clearScreen();
        printHeader("MODIFIER UN BURGER");
        
        listActiveBurgers();
        
        try {
            int id = validator.readInt("\nID du burger à modifier: ");
            Burger burger = burgerService.getBurgerById(id);
            
            System.out.println("\nBurger actuel: " + burger);
            System.out.println(ConsoleColor.info("Laissez vide pour conserver la valeur actuelle"));
            
            System.out.print("Nouveau nom [" + burger.getNom() + "]: ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) nom = burger.getNom();
            
            System.out.print("Nouveau prix [" + burger.getPrix() + "]: ");
            String prixStr = scanner.nextLine().trim();
            BigDecimal prix = prixStr.isEmpty() ? burger.getPrix() : new BigDecimal(prixStr);
            
            System.out.print("Nouveau chemin image (Entrée pour garder): ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = imagePath.isEmpty() ? null : new File(imagePath);
            
            Burger updated = burgerService.updateBurger(id, nom, prix, imageFile);
            System.out.println(ConsoleColor.success("Burger modifié avec succès!"));
            System.out.println(updated);
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void archiveBurger() {
        clearScreen();
        printHeader("ARCHIVER UN BURGER");
        
        listActiveBurgers();
        
        try {
            int id = validator.readInt("\nID du burger à archiver: ");
            Burger burger = burgerService.getBurgerById(id);
            
            System.out.println("\nBurger à archiver: " + burger);
            
            if (validator.readConfirmation("Confirmer l'archivage ?")) {
                burgerService.archiveBurger(id);
                System.out.println(ConsoleColor.success("Burger archivé avec succès!"));
            } else {
                System.out.println(ConsoleColor.info("Archivage annulé."));
            }
            
        } catch (IllegalStateException e) {
            System.out.println(ConsoleColor.error("Impossible d'archiver: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
}