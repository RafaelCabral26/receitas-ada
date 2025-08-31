// src/main/java/com/ada/resource/ReceitaResource.java
package com.ada.resource;

import com.ada.dto.ReceitaDTO;
import com.ada.model.Receita;
import com.ada.model.Usuario;
import com.ada.repository.ReceitaRepository;
import com.ada.repository.VotoRepository; // ADDED
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import io.quarkus.security.identity.SecurityIdentity;

import com.ada.dto.ReceitaDTO;
import java.util.stream.Collectors;
import java.util.List;

@Path("/receitas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceitaResource {

    @Inject
    ReceitaRepository receitaRepository;

    @Inject
    VotoRepository votoRepository; // ADDED

    @Inject
    SecurityIdentity securityIdentity;

 
    @GET
    @PermitAll
    public java.util.List<ReceitaDTO> listarReceitas() {
        // Get the current security identity (can be anonymous)
        SecurityIdentity currentIdentity = securityIdentity;

        return receitaRepository.<Receita>listAll().stream()
                .map(receita -> toReceitaDTO(receita, currentIdentity, votoRepository))
                .collect(Collectors.toList());
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

    @GET
    @Path("/my-recipes")
    @Authenticated
    public Response getMyReceitas() {
        String userEmail = securityIdentity.getPrincipal().getName();
        List<Receita> myRecipes = receitaRepository.list("autor.email", userEmail);
        return Response.ok(myRecipes).build();
    }


    @POST
    @Transactional
    @Authenticated

    public Response criarReceita(Receita receita) {
        String email = securityIdentity.getPrincipal().getName();

        // Buscar o usuário no banco de dados usando o email
        Usuario autor = Usuario.find("email", email).firstResult();

        if (autor == null) {
            // This should ideally not happen if the JWT is valid and the user exists.
            return Response.status(Response.Status.UNAUTHORIZED).entity("Usuário não encontrado.").build();
        }
// 3. Criar a nova receita e atribuir o autor
        Receita novaReceita = new Receita();
        novaReceita.titulo = receita.titulo;
        novaReceita.autor = autor;
        novaReceita.ingredientes = receita.ingredientes;
        novaReceita.descricao = receita.descricao;
        novaReceita.pathImg = receita.pathImg;
        novaReceita.persist();
        return Response.status(Response.Status.CREATED).entity(novaReceita).build();
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

        // Get the email of the logged-in user from the token
        String userEmail = securityIdentity.getPrincipal().getName();
        Usuario currentUser = Usuario.find("email", userEmail).firstResult();

        // Verify ownership by comparing the user entities
        if (currentUser == null || !existente.autor.equals(currentUser)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Você não tem permissão para editar esta receita.").build();
        }

        // Atualiza os campos
        existente.titulo = receita.titulo;
        existente.descricao = receita.descricao;
        existente.ingredientes = receita.ingredientes;
        existente.pathImg = receita.pathImg;
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

        // Get the email of the logged-in user from the token
        String userEmail = securityIdentity.getPrincipal().getName();
        Usuario currentUser = Usuario.find("email", userEmail).firstResult();

        // Verify ownership by comparing the user entities
        if (currentUser == null || !receita.autor.equals(currentUser)) {
            return Response.status(Response.Status.FORBIDDEN).entity("Você não tem permissão para excluir esta receita.").build();
        }

        receitaRepository.deleteById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private ReceitaDTO toReceitaDTO(Receita receita, SecurityIdentity securityIdentity, VotoRepository votoRepository) {
        ReceitaDTO dto = new ReceitaDTO();
        dto.setId(receita.id);
        dto.setTitulo(receita.titulo);
        dto.setDescricao(receita.descricao);
        dto.setIngredientes(receita.ingredientes);
        dto.setPathImg(receita.pathImg);
        dto.setCreatedAt(receita.createdAt);
        if (receita.autor != null) {
            dto.setAutorNome(receita.autor.username);
        }

        // Populate isAuthor and hasVoted
        if (securityIdentity == null || securityIdentity.isAnonymous()) { // Check if securityIdentity is null or anonymous
            dto.setAuthor(false);
            dto.setHasVoted(false);
        }
        else if (securityIdentity.getPrincipal() != null && securityIdentity.getPrincipal().getName() != null) { // Check for valid principal
            String userEmail = securityIdentity.getPrincipal().getName();
            Usuario currentUser = Usuario.find("email", userEmail).firstResult();

            if (currentUser != null) {
                dto.setAuthor(receita.autor != null && receita.autor.equals(currentUser));

                // Explicitly merge currentUser to ensure it's in the current persistence context
                // This might help if there are any detached entity issues
                Usuario managedCurrentUser = Usuario.findById(currentUser.id); // Re-fetch or merge

                dto.setHasVoted(votoRepository.findByUsuarioAndReceita(managedCurrentUser, receita).isPresent());
            } else {
                // User is authenticated but not found in DB (should not happen)
                dto.setAuthor(false);
                dto.setHasVoted(false);
            }
        } else {
            dto.setAuthor(false);
            dto.setHasVoted(false);
        }
        // Populate totalVotos
        dto.setTotalVotos((int) votoRepository.count("receita", receita));
        return dto;
    }
}