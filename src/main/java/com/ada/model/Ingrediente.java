package com.ada.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Ingrediente extends PanacheEntity {

    public String nome;
    public Double quantidade;
    public String unidadeMedida; // ex: "g", "ml", "colher de sopa"

    @ManyToOne
    @JsonBackReference("ingrediente-receitas")
    public Receita receita; // cada ingrediente pertence a uma receita

}
