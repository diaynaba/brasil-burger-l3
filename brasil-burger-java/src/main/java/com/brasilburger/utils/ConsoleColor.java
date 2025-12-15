
package com.brasilburger.utils;

public class ConsoleColor {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";
    
    public static String success(String text) {
        return GREEN + "✓ " + text + RESET;
    }
    
    public static String error(String text) {
        return RED + "✗ " + text + RESET;
    }
    
    public static String info(String text) {
        return CYAN + "ℹ " + text + RESET;
    }
    
    public static String warning(String text) {
        return YELLOW + "⚠ " + text + RESET;
    }
    
    public static String title(String text) {
        return BOLD + BLUE + text + RESET;
    }
}