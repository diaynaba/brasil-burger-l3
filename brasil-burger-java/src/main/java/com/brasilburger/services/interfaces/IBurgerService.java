
package com.brasilburger.services.interfaces;

import com.brasilburger.entities.Burger;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public interface IBurgerService {
    Burger createBurger(String nom, BigDecimal prix, File imageFile);
    Burger updateBurger(Integer id, String nom, BigDecimal prix, File imageFile);
    void archiveBurger(Integer id);
    Burger getBurgerById(Integer id);
    List<Burger> getAllBurgers();
    List<Burger> getActiveBurgers();
}