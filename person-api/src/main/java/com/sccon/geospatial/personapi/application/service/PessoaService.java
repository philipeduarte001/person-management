package com.sccon.geospatial.personapi.application.service;

import com.sccon.geospatial.personapi.domain.model.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaService {

    List<Pessoa> listarTodasOrdenadasPorNome();

    Optional<Pessoa> buscarPorId(Long id);

    Pessoa criarPessoa(Pessoa pessoa);

    void removerPessoa(Long id);

    Pessoa atualizarPessoa(Long id, Pessoa pessoa);

    Pessoa atualizarAtributoPessoa(Long id, String atributo, Object valor);

    long calcularIdade(Long id, String formato);

    double calcularSalario(Long id, String formato);
}
