
package com.brasilburger.repositories.interfaces;

import com.brasilburger.entities.Menu;
import java.util.List;

public interface IMenuRepository extends IRepository<Menu, Integer> {
    List<Menu> findAllActive();
    void archive(Integer id);
    Menu findByIdWithDetails(Integer id);
}