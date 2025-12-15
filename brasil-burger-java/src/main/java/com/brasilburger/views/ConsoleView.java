
package com.brasilburger.views;

import com.brasilburger.utils.ConsoleColor;
import com.brasilburger.utils.InputValidator;

import java.util.Scanner;

public class ConsoleView {
    protected final Scanner scanner;
    protected final InputValidator validator;
    
    public ConsoleView(Scanner scanner) {
        this.scanner = scanner;
        this.validator = new InputValidator(scanner);
    }
    
    protected void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    protected void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(ConsoleColor.title("  " + title));
        System.out.println("=".repeat(60));
    }
    
    protected void printSeparator() {
        System.out.println("-".repeat(60));
    }
    
    protected void pause() {
        System.out.println("\n" + ConsoleColor.info("Appuyez sur Entrée pour continuer..."));
        scanner.nextLine();
    }
}