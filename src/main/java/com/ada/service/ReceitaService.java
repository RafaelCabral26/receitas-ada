package com.ada.service;
import com.ada.repository.ReceitaRepository;
import com.ada.model.Receita;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReceitaService {

    @Inject
    ReceitaRepository receitaRepository;

    public Receita buscarPorId(Long id) {
        return receitaRepository.findById(id);
    }

    public void salvarReceita(Receita receita) {
        receitaRepository.persist(receita);
    }

    public void excluirReceita(Long id) {
        receitaRepository.deleteById(id);
    }
}