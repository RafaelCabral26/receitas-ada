// src/main/java/com/ada/repository/VotoRepository.java
package com.ada.repository;

import com.ada.model.Voto;
import com.ada.model.VotoId;
import com.ada.model.Usuario;
import com.ada.model.Receita;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class VotoRepository implements PanacheRepository<Voto, VotoId> {

    /**
     * Busca um voto específico de um usuário para uma receita.
     * @param usuario O usuário que deu o voto.
     * @param receita A receita que recebeu o voto.
     * @return Um Optional contendo o voto, se encontrado.
     */
    public Optional<Voto> findByUsuarioAndReceita(Usuario usuario, Receita receita) {
        return find("usuario =?1 and receita =?2", usuario, receita).firstResultOptional();
    }
}