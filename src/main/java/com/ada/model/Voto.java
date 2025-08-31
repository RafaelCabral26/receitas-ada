package com.ada.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voto")
public class Voto extends PanacheEntityBase {

    @EmbeddedId
    public VotoId id;

    public int valor; 

    public LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId") 
    @JoinColumn(name = "usuarioId", insertable = false, updatable = false)
    @JsonBackReference("usuario-votos") 
    public Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receitaId") 
    @JoinColumn(name = "receitaId", insertable = false, updatable = false)
    @JsonBackReference("receita-votos") 
    public Receita receita;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}