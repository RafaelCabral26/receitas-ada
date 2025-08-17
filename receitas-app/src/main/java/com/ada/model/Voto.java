// src/main/java/com/ada/model/Voto.java
package com.ada.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voto")
public class Voto extends PanacheEntityBase {

    @EmbeddedId
    public VotoId id;

    public int valor; // Exemplo de valor de votação (e.g., 1 a 5)

    public LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId", insertable = false, updatable = false)
    public Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receitaId", insertable = false, updatable = false)
    public Receita receita;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}