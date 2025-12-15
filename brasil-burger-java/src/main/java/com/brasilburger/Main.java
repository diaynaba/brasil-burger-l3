
package com.brasilburger;

import com.brasilburger.config.DatabaseConfig;
import com.brasilburger.repositories.impl.*;
import com.brasilburger.services.impl.*;
import com.brasilburger.views.*;
import com.brasilburger.utils.ConsoleColor;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
       
        displayWelcomeBanner();
        
        try {
            
            System.out.println(ConsoleColor.info("Connexion à la base de données..."));
            DatabaseConfig.getInstance();
            System.out.println(ConsoleColor.success("Base de données connectée!\n"));
            
            
            Thread.sleep(1000);
            

            var burgerRepo = new BurgerRepositoryImpl();
            var complementRepo = new ComplementRepositoryImpl();
            var menuRepo = new MenuRepositoryImpl();
            
         
            var imageService = new CloudinaryImageService();
            var burgerService = new BurgerServiceImpl(burgerRepo, imageService);
            var complementService = new ComplementServiceImpl(complementRepo, imageService);
            var menuService = new MenuServiceImpl(menuRepo, burgerService, complementService, imageService);
            
         
            var burgerView = new BurgerView(scanner, burgerService);
            var complementView = new ComplementView(scanner, complementService);
            var menuView = new MenuView(scanner, menuService, burgerService, complementService);
            
         
            mainMenu(scanner, burgerView, complementView, menuView);
            
        } catch (Exception e) {
            System.out.println(ConsoleColor.error("\n❌ Erreur fatale: " + e.getMessage()));
            System.out.println(ConsoleColor.warning("\nAssurez-vous que:"));
            System.out.println("  - Le fichier .env est correctement configuré");
            System.out.println("  - La base de données Neon est accessible");
            System.out.println("  - Les identifiants Cloudinary sont corrects");
            e.printStackTrace();
        } finally {
            scanner.close();
            DatabaseConfig.getInstance().closeConnection();
        }
    }
    
    private static void mainMenu(Scanner scanner, BurgerView burgerView, 
                                 ComplementView complementView, MenuView menuView) {
        while (true) {
            clearScreen();
            displayMainMenu();
            
            System.out.print("Votre choix: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> burgerView.displayMenu();
                case "2" -> complementView.displayMenu();
                case "3" -> menuView.displayMenu();
                case "4" -> displayAbout();
                case "5" -> displayHelp();
                case "0" -> {
                    exitApplication();
                    return;
                }
                default -> {
                    System.out.println(ConsoleColor.error("\n❌ Choix invalide! Veuillez choisir entre 0 et 5."));
                    pause(scanner);
                }
            }
        }
    }
    
    private static void displayWelcomeBanner() {
        clearScreen();
        System.out.println(ConsoleColor.CYAN + """
            
            ╔══════════════════════════════════════════════════════════════════╗
            ║                                                                  ║
            ║   ██████╗ ██████╗  █████╗ ███████╗██╗██╗                       ║
            ║   ██╔══██╗██╔══██╗██╔══██╗██╔════╝██║██║                       ║
            ║   ██████╔╝██████╔╝███████║███████╗██║██║                       ║
            ║   ██╔══██╗██╔══██╗██╔══██║╚════██║██║██║                       ║
            ║   ██████╔╝██║  ██║██║  ██║███████║██║███████╗                 ║
            ║   ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝                 ║
            ║                                                                  ║
            ║   ██████╗ ██╗   ██╗██████╗  ██████╗ ███████╗██████╗           ║
            ║   ██╔══██╗██║   ██║██╔══██╗██╔════╝ ██╔════╝██╔══██╗          ║
            ║   ██████╔╝██║   ██║██████╔╝██║  ███╗█████╗  ██████╔╝          ║
            ║   ██╔══██╗██║   ██║██╔══██╗██║   ██║██╔══╝  ██╔══██╗          ║
            ║   ██████╔╝╚██████╔╝██║  ██║╚██████╔╝███████╗██║  ██║          ║
            ║   ╚═════╝  ╚═════╝ ╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝          ║
            ║                                                                  ║
            ║              🍔 GESTION DES RESSOURCES 🍔                       ║
            ║                                                                  ║
            ║                    Version 1.0.0                                 ║
            ║              Projet L3 ISM - Semestre 1                         ║
            ║                                                                  ║
            ╚══════════════════════════════════════════════════════════════════╝
            
            """ + ConsoleColor.RESET);
    }
    
    private static void displayMainMenu() {
        System.out.println(ConsoleColor.BOLD + ConsoleColor.BLUE + 
            "\n╔════════════════════════════════════════════════════════════════╗" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.BLUE + 
            "║                      MENU PRINCIPAL                            ║" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.BLUE + 
            "╚════════════════════════════════════════════════════════════════╝" + ConsoleColor.RESET);
        
        System.out.println("\n  " + ConsoleColor.CYAN + "📋 GESTION DES RESSOURCES:" + ConsoleColor.RESET);
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │  1. 🍔 Gestion des Burgers                             │");
        System.out.println("  │  2. 🥤 Gestion des Compléments (Boissons & Frites)     │");
        System.out.println("  │  3. 📦 Gestion des Menus                               │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        
        System.out.println("\n  " + ConsoleColor.YELLOW + "ℹ️  INFORMATIONS:" + ConsoleColor.RESET);
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │  4. 📖 À propos                                         │");
        System.out.println("  │  5. ❓ Aide                                             │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        
        System.out.println("\n  " + ConsoleColor.RED + "🚪 QUITTER:" + ConsoleColor.RESET);
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │  0. Quitter l'application                               │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        
        System.out.println("\n" + "─".repeat(66));
    }
    
    private static void displayAbout() {
        clearScreen();
        System.out.println(ConsoleColor.BOLD + ConsoleColor.CYAN + 
            "\n╔════════════════════════════════════════════════════════════════╗" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.CYAN + 
            "║                        À PROPOS                                ║" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.CYAN + 
            "╚════════════════════════════════════════════════════════════════╝" + ConsoleColor.RESET);
        
        System.out.println("\n" + ConsoleColor.BOLD + "Brasil Burger - Application Console" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.info("Version: 1.0.0"));
        System.out.println(ConsoleColor.info("Date: Décembre 2024\n"));
        
        System.out.println(ConsoleColor.BOLD + "Description:" + ConsoleColor.RESET);
        System.out.println("  Application de gestion des ressources (Burgers, Compléments, Menus)");
        System.out.println("  pour le restaurant Brasil Burger.\n");
        
        System.out.println(ConsoleColor.BOLD + "Technologies utilisées:" + ConsoleColor.RESET);
        System.out.println("  • Java 17");
        System.out.println("  • Maven (Gestion de dépendances)");
        System.out.println("  • PostgreSQL (Neon Database)");
        System.out.println("  • Cloudinary (Stockage d'images)");
        System.out.println("  • Architecture SOLID\n");
        
        System.out.println(ConsoleColor.BOLD + "Fonctionnalités:" + ConsoleColor.RESET);
        System.out.println("  ✓ CRUD complet pour les Burgers");
        System.out.println("  ✓ CRUD complet pour les Compléments (Boissons et Frites)");
        System.out.println("  ✓ CRUD complet pour les Menus");
        System.out.println("  ✓ Upload d'images vers Cloudinary");
        System.out.println("  ✓ Archivage avec vérification des dépendances");
        System.out.println("  ✓ Calcul automatique des prix de menu\n");
        
        System.out.println(ConsoleColor.BOLD + "Architecture:" + ConsoleColor.RESET);
        System.out.println("  • Entity Layer (Entités métier)");
        System.out.println("  • Repository Layer (Accès aux données)");
        System.out.println("  • Service Layer (Logique métier)");
        System.out.println("  • View Layer (Interface utilisateur)\n");
        
        System.out.println(ConsoleColor.BOLD + "Projet réalisé par:" + ConsoleColor.RESET);
        System.out.println("  L3 ISM - Institut Supérieur du Management");
        System.out.println("  Projet Semestre 1 - Décembre 2024\n");
        
        System.out.println(ConsoleColor.success("© 2024 Brasil Burger. Tous droits réservés."));
        
        pause(new Scanner(System.in));
    }
    
    private static void displayHelp() {
        clearScreen();
        System.out.println(ConsoleColor.BOLD + ConsoleColor.YELLOW + 
            "\n╔════════════════════════════════════════════════════════════════╗" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.YELLOW + 
            "║                          AIDE                                  ║" + ConsoleColor.RESET);
        System.out.println(ConsoleColor.BOLD + ConsoleColor.YELLOW + 
            "╚════════════════════════════════════════════════════════════════╝" + ConsoleColor.RESET);
        
        System.out.println("\n" + ConsoleColor.BOLD + "🍔 GESTION DES BURGERS" + ConsoleColor.RESET);
        System.out.println("  • Créer : Ajoutez un nouveau burger avec nom, prix et image");
        System.out.println("  • Lister : Visualisez tous les burgers (actifs et archivés)");
        System.out.println("  • Modifier : Changez les informations d'un burger existant");
        System.out.println("  • Archiver : Désactivez un burger (impossible s'il est dans un menu actif)\n");
        
        System.out.println(ConsoleColor.BOLD + "🥤 GESTION DES COMPLÉMENTS" + ConsoleColor.RESET);
        System.out.println("  • Types disponibles : BOISSON ou FRITE");
        System.out.println("  • Créer : Ajoutez un complément avec nom, type, prix et image");
        System.out.println("  • Filtrer : Affichez uniquement les boissons ou les frites");
        System.out.println("  • Archiver : Désactivez un complément (impossible s'il est dans un menu actif)\n");
        
        System.out.println(ConsoleColor.BOLD + "📦 GESTION DES MENUS" + ConsoleColor.RESET);
        System.out.println("  • Composition : 1 Burger + 1 Boisson + 1 Frite");
        System.out.println("  • Prix : Calculé automatiquement (somme des composants)");
        System.out.println("  • Créer : Sélectionnez chaque composant dans les listes");
        System.out.println("  • Détails : Visualisez la composition complète d'un menu\n");
        
        System.out.println(ConsoleColor.BOLD + "📸 GESTION DES IMAGES" + ConsoleColor.RESET);
        System.out.println("  • Format : Chemins locaux (ex: /home/user/image.jpg)");
        System.out.println("  • Upload : Automatique vers Cloudinary lors de la création");
        System.out.println("  • Optionnel : Vous pouvez créer sans image\n");
        
        System.out.println(ConsoleColor.BOLD + "⚠️  RÈGLES D'ARCHIVAGE" + ConsoleColor.RESET);
        System.out.println("  • Un burger ne peut pas être archivé s'il est dans un menu actif");
        System.out.println("  • Un complément ne peut pas être archivé s'il est dans un menu actif");
        System.out.println("  • Archivez d'abord les menus, puis les composants\n");
        
        System.out.println(ConsoleColor.BOLD + "💡 CONSEILS" + ConsoleColor.RESET);
        System.out.println("  • Utilisez des noms descriptifs pour faciliter la recherche");
        System.out.println("  • Vérifiez les prix avant de créer un menu");
        System.out.println("  • Gardez vos images dans un dossier organisé");
        System.out.println("  • Sauvegardez régulièrement votre base de données\n");
        
        System.out.println(ConsoleColor.warning("⚠️  En cas d'erreur:"));
        System.out.println("  1. Vérifiez votre connexion internet");
        System.out.println("  2. Vérifiez les variables d'environnement (.env)");
        System.out.println("  3. Consultez les logs pour plus de détails\n");
        
        pause(new Scanner(System.in));
    }
    
    private static void exitApplication() {
        clearScreen();
        System.out.println(ConsoleColor.CYAN + "\n" + """
            ╔══════════════════════════════════════════════════════════════╗
            ║                                                              ║
            ║             Merci d'avoir utilisé Brasil Burger!            ║
            ║                                                              ║
            ║                  À bientôt! 👋🍔                            ║
            ║                                                              ║
            ╚══════════════════════════════════════════════════════════════╝
            """ + ConsoleColor.RESET);
        
        System.out.println(ConsoleColor.success("\n✓ Fermeture de la connexion à la base de données..."));
        DatabaseConfig.getInstance().closeConnection();
        System.out.println(ConsoleColor.success("✓ Application terminée proprement.\n"));
        
        System.exit(0);
    }
    
    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
           
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    private static void pause(Scanner scanner) {
        System.out.println("\n" + ConsoleColor.info("Appuyez sur Entrée pour continuer..."));
        scanner.nextLine();
    }
}