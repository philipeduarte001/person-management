package com.sccon.geospatial.personapi.infrastructure.repository;

import com.sccon.geospatial.personapi.domain.model.Person;
import com.sccon.geospatial.personapi.domain.repository.PersonRepository;
import com.sccon.geospatial.personapi.infrastructure.repository.jpa.PersonJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class PersonRepositoryImpl implements PersonRepository {

    private final PersonJpaRepository jpaRepository;

    public PersonRepositoryImpl(PersonJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Person save(Person person) {
        log.debug("Salvando pessoa: {}", person.getName());
        return jpaRepository.save(person);
    }

    @Override
    public Optional<Person> findById(Long id) {
        log.debug("Buscando pessoa por ID: {}", id);
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Person> findByCpf(String cpf) {
        log.debug("Buscando pessoa por CPF: {}", cpf);
        return jpaRepository.findByCpf(cpf);
    }

    @Override
    public List<Person> findAll() {
        log.debug("Buscando todas as pessoas");
        return jpaRepository.findAll();
    }

    @Override
    public List<Person> findByNameContainingIgnoreCase(String name) {
        log.debug("Buscando pessoas por nome: {}", name);
        return jpaRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        log.debug("Verificando existência de pessoa por CPF: {}", cpf);
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Removendo pessoa por ID: {}", id);
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        log.debug("Contando total de pessoas");
        return jpaRepository.count();
    }
}
