// src/main/java/com/ada/repository/VotoRepository.java
package com.ada.repository;

import com.ada.model.Voto;
import com.ada.model.VotoId;
import com.ada.model.Usuario;
import com.ada.model.Receita;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class VotoRepository implements PanacheRepositoryBase<Voto, VotoId> {

    /**
     * Busca um voto específico de um usuário para uma receita.
     * @param usuario O usuário que deu o voto.
     * @param receita A receita que recebeu o voto.
     * @return Um Optional contendo o voto, se encontrado.
     */
    public Optional<Voto> findByUsuarioAndReceita(Usuario usuario, Receita receita) {
        // Create a VotoId directly and use findById
        VotoId votoId = new VotoId(usuario.id, receita.id);
        return findByIdOptional(votoId);
    }
}