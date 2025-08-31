// src/main/java/com/ada/repository/UsuarioRepository.java
package com.ada.repository;

import com.ada.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {


    public Optional<Usuario> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}