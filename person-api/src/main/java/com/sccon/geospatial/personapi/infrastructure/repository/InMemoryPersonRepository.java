package com.sccon.geospatial.personapi.infrastructure.repository;

import com.sccon.geospatial.personapi.domain.model.Person;
import com.sccon.geospatial.personapi.domain.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Repository
@Primary
@Slf4j
public class InMemoryPersonRepository implements PersonRepository {

    private final Map<Long, Person> personMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Person save(Person person) {
        log.debug("Salvando pessoa no mapa em memória: {}", person.getName());
        
        if (person.getId() == null) {
            person.setId(idGenerator.getAndIncrement());
            person.setCreatedAt(LocalDateTime.now());
            person.setUpdatedAt(LocalDateTime.now());
        } else {
            Person existingPerson = personMap.get(person.getId());
            if (existingPerson != null) {
                person.setCreatedAt(existingPerson.getCreatedAt());
            }
            person.setUpdatedAt(LocalDateTime.now());
        }
        
        personMap.put(person.getId(), person);
        log.info("Pessoa salva no mapa com ID: {}", person.getId());
        
        return person;
    }

    @Override
    public Optional<Person> findById(Long id) {
        log.debug("Buscando pessoa por ID no mapa: {}", id);
        Person person = personMap.get(id);
        return Optional.ofNullable(person);
    }

    @Override
    public Optional<Person> findByCpf(String cpf) {
        log.debug("Buscando pessoa por CPF no mapa: {}", cpf);
        return personMap.values().stream()
                .filter(person -> Objects.equals(person.getCpf(), cpf))
                .findFirst();
    }

    @Override
    public List<Person> findAll() {
        log.debug("Buscando todas as pessoas no mapa");
        return new ArrayList<>(personMap.values());
    }

    @Override
    public List<Person> findByNameContainingIgnoreCase(String name) {
        log.debug("Buscando pessoas por nome no mapa: {}", name);
        return personMap.values().stream()
                .filter(person -> person.getName() != null && 
                        person.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCpf(String cpf) {
        log.debug("Verificando existência de pessoa por CPF no mapa: {}", cpf);
        return personMap.values().stream()
                .anyMatch(person -> Objects.equals(person.getCpf(), cpf));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Removendo pessoa por ID do mapa: {}", id);
        Person removed = personMap.remove(id);
        if (removed != null) {
            log.info("Pessoa removida do mapa com ID: {}", id);
        } else {
            log.warn("Pessoa não encontrada para remoção com ID: {}", id);
        }
    }

    @Override
    public long count() {
        log.debug("Contando total de pessoas no mapa");
        return personMap.size();
    }

    public Map<Long, Person> getPersonMap() {
        return new HashMap<>(personMap);
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPersons", personMap.size());
        stats.put("nextId", idGenerator.get());
        stats.put("ids", new ArrayList<>(personMap.keySet()));
        return stats;
    }
}
