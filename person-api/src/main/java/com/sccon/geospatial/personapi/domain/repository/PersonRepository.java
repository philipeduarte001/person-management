package com.sccon.geospatial.personapi.domain.repository;

import com.sccon.geospatial.personapi.domain.model.Person;

import java.util.List;
import java.util.Optional;


public interface PersonRepository {

    Person save(Person person);

    Optional<Person> findById(Long id);

    Optional<Person> findByCpf(String cpf);

    List<Person> findAll();

    List<Person> findByNameContainingIgnoreCase(String name);

    boolean existsByCpf(String cpf);

    void deleteById(Long id);

    long count();
}
