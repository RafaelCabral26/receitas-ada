// src/main/java/com/ada/resource/VotoResource.java
package com.ada.resource;

import com.ada.model.Receita;
import com.ada.model.Usuario;
import com.ada.model.Voto;
import com.ada.model.VotoId;
import com.ada.repository.ReceitaRepository;
import com.ada.repository.UsuarioRepository;
import com.ada.repository.VotoRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.quarkus.security.identity.SecurityIdentity;

@Path("/votos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotoResource {

    @Inject
    VotoRepository votoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    ReceitaRepository receitaRepository;

    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Endpoint para um usuário votar em uma receita.
     * Garante que o usuário vote apenas uma vez por receita e não em sua própria receita.
     * @param idReceita O ID da receita que receberá o voto.
     * @param valor O valor do voto.
     * @return Resposta HTTP de sucesso ou erro.
     */
    @POST
    @Path("/{idReceita}/{valor}")
    @Transactional
    @RolesAllowed("user")
    public Response votar(
            @PathParam("idReceita") Long idReceita,
            @PathParam("valor") int valor) {

        String userEmail = securityIdentity.getPrincipal().getName();
        Usuario usuario = Usuario.find("email", userEmail).firstResult();
        Receita receita = receitaRepository.findById(idReceita);

        if (usuario == null || receita == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Verifica se o usuário está tentando votar em sua própria receita
        if (receita.autor.id.equals(usuario.id)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Você não pode votar em sua própria receita.").build();
        }

        // Verifica se o usuário já votou nesta receita
        if (votoRepository.findByUsuarioAndReceita(usuario, receita).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Você já votou nesta receita.").build();
        }

        Voto voto = new Voto();
        voto.usuario = usuario;
        voto.receita = receita;
        voto.valor = valor;

        // Cria a chave composta
        VotoId votoId = new VotoId(usuario.id, receita.id);
        voto.id = votoId;

        votoRepository.persist(voto);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{idReceita}")
    @Transactional
    @RolesAllowed("user")
    public Response removerVoto(@PathParam("idReceita") Long idReceita) {
        String userEmail = securityIdentity.getPrincipal().getName();
        Usuario usuario = Usuario.find("email", userEmail).firstResult();
        Receita receita = receitaRepository.findById(idReceita);

        if (usuario == null || receita == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Find the vote to remove
        Voto voto = votoRepository.findByUsuarioAndReceita(usuario, receita).orElse(null);

        if (voto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Voto não encontrado para remoção.").build();
        }

        votoRepository.delete(voto);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}