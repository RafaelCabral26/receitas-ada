package com.ada.repository;

import com.ada.model.ReceitaCategoria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaRepository implements PanacheRepository<ReceitaCategoria> {
}
