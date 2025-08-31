package com.ada.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Receita extends PanacheEntity {

    public String titulo;
    public String descricao;
    public String ingredientes;
    public String pathImg;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    @JsonBackReference("usuario-receitas") // Adicionado
    public Usuario autor;

    @OneToMany(mappedBy = "receita")
    @JsonManagedReference("receita-votos") // Adicionado
    @JsonIgnore
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