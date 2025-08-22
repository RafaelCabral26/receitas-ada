// src/main/java/com/ada/model/Receita.java
package com.ada.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Receita extends PanacheEntity {

    public String titulo;
    public String descricao;
    public String ingredientes;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    public Usuario autor;

    // Relação many-to-many com Voto via entidade de junção
    @OneToMany(mappedBy = "receita")
    public List<Voto> votos;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}