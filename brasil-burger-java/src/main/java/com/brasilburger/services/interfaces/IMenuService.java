
package com.brasilburger.services.interfaces;

import com.brasilburger.entities.Menu;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public interface IMenuService {
    Menu createMenu(String nom, File imageFile, Integer burgerId, Integer boissonId, Integer friteId);
    Menu updateMenu(Integer id, String nom, File imageFile, Integer burgerId, Integer boissonId, Integer friteId);
    void archiveMenu(Integer id);
    Menu getMenuById(Integer id);
    Menu getMenuWithDetails(Integer id);
    List<Menu> getAllMenus();
    List<Menu> getActiveMenus();
    BigDecimal calculateMenuPrice(Integer burgerId, Integer boissonId, Integer friteId);
}