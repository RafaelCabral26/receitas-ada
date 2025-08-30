package com.ada.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ReceitaCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String nome;

    // Opcional: se quiser ver as receitas associadas
    @OneToMany(mappedBy = "categoria")
    public List<Receita> receitas;

}
