// src/main/java/com/ada/resource/ReceitaResource.java
package com.ada.resource;

import com.ada.model.Receita;
import com.ada.model.Usuario;
import com.ada.repository.ReceitaRepository;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import io.quarkus.security.identity.SecurityIdentity;

import java.util.List;

@Path("/receitas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceitaResource {

    @Inject
    ReceitaRepository receitaRepository;

    @Inject
    SecurityIdentity securityIdentity;

 
    @GET
    @PermitAll
    public List<Receita> listarReceitas() {
        return receitaRepository.listAll();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    public Response getReceita(@PathParam("id") Long id) {
        Receita receita = receitaRepository.findById(id);
        if (receita == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(receita).build();
    }


    @POST
    @Transactional
    @Authenticated

    public Response criarReceita(Receita receita) {
        String username = securityIdentity.getPrincipal().getName();

// 2. Buscar o usuário no banco de dados usando o nome de usuário
        Usuario autor = Usuario.find("username", username).firstResult();

        if (autor == null) {
        return Response.ok(Response.Status.UNAUTHORIZED).build();
        }
// 3. Criar a nova receita e atribuir o autor
        Receita novaReceita = new Receita();
        novaReceita.titulo = receita.titulo;
        novaReceita.autor = autor;
        novaReceita.ingredientes = receita.ingredientes;
        novaReceita.descricao = receita.descricao;
        novaReceita.persist();
        return Response.ok(Response.Status.CREATED).build();
    }

    /**
     * Endpoint para atualizar uma receita existente.
     * Permite a edição apenas se o usuário autenticado for o autor.
     *
     * @param id      O ID da receita a ser atualizada.
     * @param receita A receita com as informações atualizadas.
     * @return A receita atualizada.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    @RolesAllowed("user")
    public Response editarReceita(@PathParam("id") Long id, Receita receita) {
        Receita existente = receitaRepository.findById(id);
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Verifica se o usuário autenticado é o autor da receita
        if (!existente.autor.id.equals(Long.valueOf(securityIdentity.getAttribute("id").toString()))) {
            return Response.status(Response.Status.FORBIDDEN).entity("Você não tem permissão para editar esta receita.").build();
        }

        // Atualiza os campos
        existente.titulo = receita.titulo;
        existente.descricao = receita.descricao;
        existente.ingredientes = receita.ingredientes;
        receitaRepository.persist(existente);
        return Response.ok(existente).build();
    }

    /**
     * Endpoint para excluir uma receita.
     * Permite a exclusão apenas se o usuário autenticado for o autor.
     *
     * @param id O ID da receita a ser excluída.
     * @return Resposta HTTP de sucesso ou erro.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    @RolesAllowed("user")
    public Response excluirReceita(@PathParam("id") Long id) {
        Receita receita = receitaRepository.findById(id);
        if (receita == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Verifica se o usuário autenticado é o autor da receita
        if (!receita.autor.id.equals(Long.valueOf(securityIdentity.getAttribute("id").toString()))) {
            return Response.status(Response.Status.FORBIDDEN).entity("Você não tem permissão para excluir esta receita.").build();
        }

        receitaRepository.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}