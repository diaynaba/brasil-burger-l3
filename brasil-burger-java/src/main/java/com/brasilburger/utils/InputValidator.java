
package com.brasilburger.utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputValidator {
    private final Scanner scanner;
    
    public InputValidator(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public String readNonEmptyString(String prompt) {
        String input;
        do {
            input = readString(prompt);
            if (input.isEmpty()) {
                System.out.println(ConsoleColor.error("Ce champ est obligatoire!"));
            }
        } while (input.isEmpty());
        return input;
    }
    
    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColor.error("Veuillez entrer un nombre valide!"));
            }
        }
    }
    
    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println(ConsoleColor.error("Le montant doit être supérieur à 0!"));
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColor.error("Veuillez entrer un montant valide!"));
            }
        }
    }
    
    public boolean readConfirmation(String prompt) {
        String response = readString(prompt + " (o/n): ").toLowerCase();
        return response.equals("o") || response.equals("oui");
    }
}