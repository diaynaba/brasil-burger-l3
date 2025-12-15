
package com.brasilburger.repositories.interfaces;

import com.brasilburger.entities.Burger;
import java.util.List;

public interface IBurgerRepository extends IRepository<Burger, Integer> {
    List<Burger> findAllActive();
    void archive(Integer id);
    boolean isUsedInActiveMenus(Integer burgerId);
}