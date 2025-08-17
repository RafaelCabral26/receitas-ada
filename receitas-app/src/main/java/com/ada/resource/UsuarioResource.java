// src/main/java/com/ada/resource/UsuarioResource.java
package com.ada.resource;

import com.ada.dto.LoginDTO;
import com.ada.model.Usuario;
import com.ada.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Endpoint para o registro de um novo usuário.
     * @param usuario O objeto Usuario a ser criado.
     * @return Resposta HTTP de sucesso ou erro.
     */
    @POST
    @Transactional
    @PermitAll
    public Response criarUsuario(Usuario usuario) {
        if (usuario.username == null || usuario.username.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username não pode ser vazio.").build();
        }
        if (usuarioRepository.findByUsername(usuario.username).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Username já existe.").build();
        }
        Usuario.add(usuario.username, usuario.password, "user");
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Endpoint para autenticação de usuário e geração de JWT.
     * @param loginDTO Objeto contendo as credenciais de login.
     * @return Resposta HTTP com o token JWT ou erro.
     */
    @POST
    @Path("/login")
    @PermitAll
    public Response login(LoginDTO loginDTO) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(loginDTO.username);

        if (usuario.isEmpty() ||!BcryptUtil.matches(loginDTO.password, usuario.get().password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas.").build();
        }

        // Gera o token JWT com as informações do usuário
        //O issuer deve ser configurado no application.properties
        String token = Jwt.issuer("receitas-app-api")
                .upn(usuario.get().username) // User Principal Name
                .groups(usuario.get().role) // Papéis do usuário
                .sign();

        return Response.ok(token).build();
    }

    /**
     * Endpoint para logout.
     * O logout em sistemas JWT é uma operação no lado do cliente.
     * Este endpoint é simbólico e não realiza nenhuma ação no servidor.
     * @return Resposta HTTP de sucesso.
     */
    @POST
    @Path("/logout")
    @PermitAll
    public Response logout() {
        return Response.ok().entity("Logout bem-sucedido. O token deve ser descartado pelo cliente.").build();
    }
}