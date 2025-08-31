// src/main/java/com/ada/model/Usuario.java
package com.ada.model;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Usuario extends PanacheEntity {


    public String email;
   
    public String username;
    public String password;
    

    public String role;

    @OneToMany(mappedBy = "autor")
  @JsonManagedReference("usuario-receitas")
      public List<Receita> receitas;

    // Relação many-to-many com Voto via entidade de junção
    @OneToMany(mappedBy = "usuario")
  @JsonManagedReference("usuario-votos")
      public List<Voto> votos;


    public static void add(String email, String username, String password, String role) {
        Usuario user = new Usuario();
        user.email = email;
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();
    }
}