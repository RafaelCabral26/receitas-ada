package com.ada.resource;

import com.ada.model.ReceitaCategoria;
import com.ada.repository.CategoriaRepository;
import com.ada.repository.CategoriaRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/categorias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceitaCategoriaResource {

    @Inject
    CategoriaRepository categoriaRepository;

    @GET
    public List<ReceitaCategoria> listarCategorias() {
        return categoriaRepository.listAll();
    }

    @POST
    @Transactional
    public Response criarCategoria(ReceitaCategoria categoria) {
        categoriaRepository.persist(categoria);
        return Response.status(Response.Status.CREATED).entity(categoria).build();
    }
}