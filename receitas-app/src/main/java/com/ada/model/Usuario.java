// src/main/java/com/ada/model/Usuario.java
package com.ada.model;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "receitas_user")
@UserDefinition
public class Usuario extends PanacheEntity {

    @Username
    public String username;

    @Password
    public String password;

    @Roles
    public String role;

    @OneToMany(mappedBy = "autor")
    public List<Receita> receitas;

    // Relação many-to-many com Voto via entidade de junção
    @OneToMany(mappedBy = "usuario")
    public List<Voto> votos;

    /**
     * Adiciona um novo usuário ao banco de dados com a senha criptografada.
     * @param username O nome de usuário.
     * @param password A senha não criptografada.
     * @param role O papel do usuário.
     */
    public static void add(String username, String password, String role) {
        Usuario user = new Usuario();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();
    }
}