
package com.brasilburger.repositories.interfaces;

import com.brasilburger.entities.Complement;
import com.brasilburger.entities.TypeComplement;
import java.util.List;

public interface IComplementRepository extends IRepository<Complement, Integer> {
    List<Complement> findAllActive();
    List<Complement> findByType(TypeComplement type);
    void archive(Integer id);
    boolean isUsedInActiveMenus(Integer complementId);
}