// src/main/java/com/ada/repository/ReceitaRepository.java
package com.ada.repository;

import com.ada.model.Receita;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReceitaRepository implements PanacheRepository<Receita> {
    // Métodos personalizados podem ser adicionados aqui.
    public Receita findByTitulo(String titulo) {
        return find("titulo", titulo).firstResult();
    }
}