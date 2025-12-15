// ========== MenuHelper.java ==========
package com.brasilburger.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Classe utilitaire pour les opérations communes sur les menus
 */
public class MenuHelper {
    
    /**
     * Formate un prix en FCFA
     */
    public static String formatPrice(BigDecimal price) {
        if (price == null) {
            return "0 FCFA";
        }
        NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
        return formatter.format(price) + " FCFA";
    }
    
    /**
     * Calcule le pourcentage de réduction
     */
    public static BigDecimal calculateDiscount(BigDecimal originalPrice, BigDecimal discountedPrice) {
        if (originalPrice == null || discountedPrice == null || 
            originalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal difference = originalPrice.subtract(discountedPrice);
        return difference.multiply(new BigDecimal("100"))
                        .divide(originalPrice, 2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * Valide un nom de menu
     */
    public static boolean isValidMenuName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }
    
    /**
     * Nettoie un nom de menu (enlève espaces superflus)
     */
    public static String cleanMenuName(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Génère un code unique pour un menu
     */
    public static String generateMenuCode(String menuName) {
        if (menuName == null || menuName.isEmpty()) {
            return "MENU-" + System.currentTimeMillis();
        }
        
        String cleaned = menuName.toUpperCase()
                                 .replaceAll("[^A-Z0-9]", "")
                                 .substring(0, Math.min(menuName.length(), 5));
        
        return "MENU-" + cleaned + "-" + System.currentTimeMillis() % 10000;
    }
    
    /**
     * Vérifie si un prix est valide
     */
    public static boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Arrondit un prix au millier supérieur (pour FCFA)
     */
    public static BigDecimal roundToNearestThousand(BigDecimal price) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal thousand = new BigDecimal("1000");
        return price.divide(thousand, 0, BigDecimal.ROUND_UP)
                   .multiply(thousand);
    }
    
    /**
     * Calcule une remise en pourcentage
     */
    public static BigDecimal applyDiscount(BigDecimal originalPrice, int discountPercent) {
        if (originalPrice == null || discountPercent < 0 || discountPercent > 100) {
            return originalPrice;
        }
        
        BigDecimal percent = new BigDecimal(discountPercent);
        BigDecimal discount = originalPrice.multiply(percent)
                                          .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        
        return originalPrice.subtract(discount);
    }
    
    /**
     * Formate une description de menu
     */
    public static String formatMenuDescription(String burgerName, String drinkName, String friteName) {
        return String.format("%s + %s + %s", 
            burgerName != null ? burgerName : "Burger", 
            drinkName != null ? drinkName : "Boisson", 
            friteName != null ? friteName : "Frites");
    }
    
    /**
     * Vérifie si un menu est complet (a tous ses composants)
     */
    public static boolean isMenuComplete(Integer burgerId, Integer drinkId, Integer friteId) {
        return burgerId != null && drinkId != null && friteId != null;
    }
}

