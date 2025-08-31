// src/main/java/com/ada/resource/UsuarioResource.java
package com.ada.resource;

import com.ada.dto.LoginDTO;
import com.ada.model.Usuario;
import com.ada.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Optional;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    SecurityIdentity securityIdentity;


    @POST
    @Transactional
    @PermitAll
    public Response criarUsuario(Usuario usuario) {
        if (usuario.email == null || usuario.email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("email não pode ser vazio.").build();
        }
        if (usuarioRepository.findByEmail(usuario.email).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Email já existe.").build();
        }
        Usuario.add(usuario.email, usuario.username, usuario.password, "user");
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
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginDTO.email);
        if (usuario.isEmpty() ||!BcryptUtil.matches(loginDTO.password, usuario.get().password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas.").build();
        }

        // Gera o token JWT com as informações do usuário
        //O issuer deve ser configurado no application.properties
                String token = Jwt.issuer("receitas-app-api")
                .upn(usuario.get().email) // User Principal Name
                .claim("id", usuario.get().id)
                .claim("username", usuario.get().username)
                .groups(usuario.get().role) // Papéis do usuário
                .sign();

        return Response.ok(Map.of("token", token)).build();
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
        return Response.noContent().build();
    }

    @GET
    @Path("/validar_usuario")
    @Authenticated
     public Response validarUsuario() {
        String email = securityIdentity.getPrincipal().getName();
        return usuarioRepository.findByEmail(email)
                // Simplesmente retorne o objeto 'usuario' encontrado.
                // O JSON resultante incluirá id, email, username e role.
                .map(usuario -> Response.ok(usuario).build()) 
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}