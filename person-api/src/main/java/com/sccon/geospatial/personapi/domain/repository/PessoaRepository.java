package com.sccon.geospatial.personapi.domain.repository;

import com.sccon.geospatial.personapi.domain.model.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaRepository {

    Pessoa save(Pessoa pessoa);

    Optional<Pessoa> findById(Long id);

    List<Pessoa> findAllOrderByNome();

    boolean existsById(Long id);

    void deleteById(Long id);

    long count();

    Long getNextId();
}
