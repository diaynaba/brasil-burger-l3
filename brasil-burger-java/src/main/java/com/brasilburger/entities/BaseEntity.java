
package com.brasilburger.entities;

import java.time.LocalDateTime;

public abstract class BaseEntity {
    protected Integer id;
    protected LocalDateTime dateCreation;
    
    public BaseEntity() {
        this.dateCreation = LocalDateTime.now();
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}