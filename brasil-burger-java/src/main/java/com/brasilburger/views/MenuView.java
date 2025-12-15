
package com.brasilburger.views;

import com.brasilburger.entities.*;
import com.brasilburger.services.interfaces.*;
import com.brasilburger.utils.ConsoleColor;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MenuView extends ConsoleView {
    private final IMenuService menuService;
    private final IBurgerService burgerService;
    private final IComplementService complementService;
    
    public MenuView(Scanner scanner, IMenuService menuService, 
                   IBurgerService burgerService, IComplementService complementService) {
        super(scanner);
        this.menuService = menuService;
        this.burgerService = burgerService;
        this.complementService = complementService;
    }
    
    public void displayMenu() {
        while (true) {
            clearScreen();
            printHeader("GESTION DES MENUS 🍔🥤🍟");
            System.out.println("1. Créer un menu");
            System.out.println("2. Lister tous les menus");
            System.out.println("3. Voir détails d'un menu");
            System.out.println("4. Modifier un menu");
            System.out.println("5. Archiver un menu");
            System.out.println("6. Lister menus actifs uniquement");
            System.out.println("0. Retour");
            printSeparator();
            
            int choice = validator.readInt("Votre choix: ");
            
            switch (choice) {
                case 1 -> createMenu();
                case 2 -> listAllMenus();
                case 3 -> viewMenuDetails();
                case 4 -> updateMenu();
                case 5 -> archiveMenu();
                case 6 -> listActiveMenus();
                case 0 -> { return; }
                default -> System.out.println(ConsoleColor.error("Choix invalide!"));
            }
        }
    }
    
    private void createMenu() {
        clearScreen();
        printHeader("CRÉER UN MENU");
        
        try {
            String nom = validator.readNonEmptyString("Nom du menu: ");
            
           
            System.out.println("\n" + ConsoleColor.title("=== SÉLECTION DU BURGER ==="));
            List<Burger> burgers = burgerService.getActiveBurgers();
            if (burgers.isEmpty()) {
                System.out.println(ConsoleColor.error("Aucun burger actif disponible!"));
                pause();
                return;
            }
            displayBurgersForSelection(burgers);
            int burgerChoice = validator.readInt("Choisir un burger (ID): ");
            
           
            Burger selectedBurger = burgers.stream()
                .filter(b -> b.getId().equals(burgerChoice))
                .findFirst()
                .orElse(null);
            
            if (selectedBurger == null) {
                System.out.println(ConsoleColor.error("Burger invalide!"));
                pause();
                return;
            }
            
        
            System.out.println("\n" + ConsoleColor.title("=== SÉLECTION DE LA BOISSON ==="));
            List<Complement> boissons = complementService.getComplementsByType(TypeComplement.BOISSON);
            if (boissons.isEmpty()) {
                System.out.println(ConsoleColor.error("Aucune boisson active disponible!"));
                pause();
                return;
            }
            displayComplementsForSelection(boissons);
            int boissonChoice = validator.readInt("Choisir une boisson (ID): ");
            
            Complement selectedBoisson = boissons.stream()
                .filter(c -> c.getId().equals(boissonChoice))
                .findFirst()
                .orElse(null);
            
            if (selectedBoisson == null) {
                System.out.println(ConsoleColor.error("Boisson invalide!"));
                pause();
                return;
            }
            
          
            System.out.println("\n" + ConsoleColor.title("=== SÉLECTION DES FRITES ==="));
            List<Complement> frites = complementService.getComplementsByType(TypeComplement.FRITE);
            if (frites.isEmpty()) {
                System.out.println(ConsoleColor.error("Aucune frite active disponible!"));
                pause();
                return;
            }
            displayComplementsForSelection(frites);
            int friteChoice = validator.readInt("Choisir des frites (ID): ");
            
            Complement selectedFrite = frites.stream()
                .filter(c -> c.getId().equals(friteChoice))
                .findFirst()
                .orElse(null);
            
            if (selectedFrite == null) {
                System.out.println(ConsoleColor.error("Frites invalides!"));
                pause();
                return;
            }
            
           
            BigDecimal prixTotal = menuService.calculateMenuPrice(
                burgerChoice, boissonChoice, friteChoice
            );
            
            
            System.out.println("\n" + ConsoleColor.title("=== RÉCAPITULATIF DU MENU ==="));
            System.out.println("Nom du menu: " + ConsoleColor.info(nom));
            System.out.println("Burger: " + selectedBurger.getNom() + " (" + selectedBurger.getPrix() + " FCFA)");
            System.out.println("Boisson: " + selectedBoisson.getNom() + " (" + selectedBoisson.getPrix() + " FCFA)");
            System.out.println("Frites: " + selectedFrite.getNom() + " (" + selectedFrite.getPrix() + " FCFA)");
            System.out.println(ConsoleColor.success("Prix total: " + prixTotal + " FCFA"));
            printSeparator();
            
            if (!validator.readConfirmation("Confirmer la création du menu ?")) {
                System.out.println(ConsoleColor.info("Création annulée."));
                pause();
                return;
            }
            
           
            System.out.print("Chemin de l'image du menu (ou Entrée pour ignorer): ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = imagePath.isEmpty() ? null : new File(imagePath);
            
            if (imageFile != null && !imageFile.exists()) {
                System.out.println(ConsoleColor.warning("Fichier image non trouvé. Création sans image."));
                imageFile = null;
            }
            
            Menu menu = menuService.createMenu(nom, imageFile, burgerChoice, boissonChoice, friteChoice);
            System.out.println(ConsoleColor.success("\n✓ Menu créé avec succès! ID: " + menu.getId()));
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
            e.printStackTrace();
        }
        
        pause();
    }
    
    private void listAllMenus() {
        clearScreen();
        printHeader("LISTE DES MENUS");
        
        List<Menu> menus = menuService.getAllMenus();
        
        if (menus.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun menu trouvé."));
        } else {
            displayMenusTable(menus);
        }
        
        pause();
    }
    
    private void listActiveMenus() {
        clearScreen();
        printHeader("MENUS ACTIFS");
        
        List<Menu> menus = menuService.getActiveMenus();
        
        if (menus.isEmpty()) {
            System.out.println(ConsoleColor.info("Aucun menu actif."));
        } else {
            displayMenusTable(menus);
        }
        
        pause();
    }
    
    private void viewMenuDetails() {
        clearScreen();
        printHeader("DÉTAILS D'UN MENU");
        
        listActiveMenus();
        
        try {
            int id = validator.readInt("\nID du menu: ");
            Menu menu = menuService.getMenuWithDetails(id);
            
            clearScreen();
            printHeader("DÉTAILS DU MENU #" + id);
            
            System.out.println("Nom: " + ConsoleColor.info(menu.getNom()));
            System.out.println("Statut: " + (menu.isEstArchive() ? 
                ConsoleColor.warning("Archivé") : ConsoleColor.success("Actif")));
            printSeparator();
            
            System.out.println("\n🍔 BURGER:");
            System.out.println("   " + menu.getBurger().getNom() + " - " + menu.getBurger().getPrix() + " FCFA");
            
            System.out.println("\n🥤 BOISSON:");
            System.out.println("   " + menu.getBoisson().getNom() + " - " + menu.getBoisson().getPrix() + " FCFA");
            
            System.out.println("\n🍟 FRITES:");
            System.out.println("   " + menu.getFrite().getNom() + " - " + menu.getFrite().getPrix() + " FCFA");
            
            printSeparator();
            System.out.println(ConsoleColor.success("💰 PRIX TOTAL: " + menu.calculerPrixTotal() + " FCFA"));
            
            if (menu.getImage() != null && !menu.getImage().isEmpty()) {
                System.out.println("\n📸 Image: " + menu.getImage());
            }
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void updateMenu() {
        clearScreen();
        printHeader("MODIFIER UN MENU");
        
        listActiveMenus();
        
        try {
            int id = validator.readInt("\nID du menu à modifier: ");
            Menu menu = menuService.getMenuWithDetails(id);
            
            System.out.println("\nMenu actuel: " + menu);
            System.out.println(ConsoleColor.info("Laissez vide pour conserver la valeur actuelle"));
            
            System.out.print("Nouveau nom [" + menu.getNom() + "]: ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) nom = menu.getNom();
            
            
            
            System.out.print("Nouveau chemin image (Entrée pour garder): ");
            String imagePath = scanner.nextLine().trim();
            File imageFile = imagePath.isEmpty() ? null : new File(imagePath);
            
            Menu updated = menuService.updateMenu(id, nom, imageFile, 
                menu.getBurgerId(), menu.getBoissonId(), menu.getFriteId());
            
            System.out.println(ConsoleColor.success("Menu modifié avec succès!"));
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void archiveMenu() {
        clearScreen();
        printHeader("ARCHIVER UN MENU");
        
        listActiveMenus();
        
        try {
            int id = validator.readInt("\nID du menu à archiver: ");
            Menu menu = menuService.getMenuById(id);
            
            System.out.println("\nMenu à archiver: " + menu);
            
            if (validator.readConfirmation("Confirmer l'archivage ?")) {
                menuService.archiveMenu(id);
                System.out.println(ConsoleColor.success("Menu archivé avec succès!"));
            } else {
                System.out.println(ConsoleColor.info("Archivage annulé."));
            }
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("Erreur: " + e.getMessage()));
        }
        
        pause();
    }
    
    private void displayMenusTable(List<Menu> menus) {
        System.out.printf("%-5s %-30s %-10s%n", "ID", "NOM", "STATUT");
        printSeparator();
        
        for (Menu m : menus) {
            String statut = m.isEstArchive() ? 
                ConsoleColor.warning("Archivé") : 
                ConsoleColor.success("Actif");
            
            System.out.printf("%-5d %-30s %s%n", 
                m.getId(), 
                m.getNom(), 
                statut);
        }
        
        System.out.println("\nTotal: " + menus.size() + " menu(s)");
    }
    
    private void displayBurgersForSelection(List<Burger> burgers) {
        System.out.printf("%-5s %-30s %-15s%n", "ID", "NOM", "PRIX (FCFA)");
        printSeparator();
        for (Burger b : burgers) {
            System.out.printf("%-5d %-30s %-15.2f%n", b.getId(), b.getNom(), b.getPrix());
        }
    }
    
    private void displayComplementsForSelection(List<Complement> complements) {
        System.out.printf("%-5s %-30s %-15s%n", "ID", "NOM", "PRIX (FCFA)");
        printSeparator();
        for (Complement c : complements) {
            System.out.printf("%-5d %-30s %-15.2f%n", c.getId(), c.getNom(), c.getPrix());
        }
    }
}