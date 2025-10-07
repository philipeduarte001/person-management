package com.sccon.geospatial.personapi.application.usecase.impl;

import com.sccon.geospatial.personapi.application.dto.PersonRequestDto;
import com.sccon.geospatial.personapi.application.dto.PersonResponseDto;
import com.sccon.geospatial.personapi.application.mapper.PersonMapper;
import com.sccon.geospatial.personapi.application.usecase.PersonUseCase;
import com.sccon.geospatial.personapi.domain.model.Person;
import com.sccon.geospatial.personapi.domain.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PersonUseCaseImpl implements PersonUseCase {

    private final PersonService personService;
    private final PersonMapper personMapper;

    public PersonUseCaseImpl(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @Override
    @Transactional
    public CompletableFuture<PersonResponseDto> createPerson(PersonRequestDto requestDto) {
        log.debug("Iniciando criação de pessoa de forma assíncrona: {}", requestDto.getName());
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Person person = personMapper.toEntity(requestDto);
                Person createdPerson = personService.createPerson(person);
                return personMapper.toResponseDto(createdPerson);
            } catch (Exception e) {
                log.error("Erro ao criar pessoa: {}", e.getMessage(), e);
                throw new RuntimeException("Erro ao criar pessoa: " + e.getMessage(), e);
            }
        });
    }

    @Override
    @Transactional
    public CompletableFuture<PersonResponseDto> updatePerson(Long id, PersonRequestDto requestDto) {
        log.debug("Iniciando atualização de pessoa de forma assíncrona. ID: {}", id);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Person person = personMapper.toEntity(requestDto);
                Person updatedPerson = personService.updatePerson(id, person);
                return personMapper.toResponseDto(updatedPerson);
            } catch (Exception e) {
                log.error("Erro ao atualizar pessoa com ID {}: {}", id, e.getMessage(), e);
                throw new RuntimeException("Erro ao atualizar pessoa: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<Optional<PersonResponseDto>> findPersonById(Long id) {
        log.debug("Iniciando busca de pessoa por ID de forma assíncrona: {}", id);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                return personService.findPersonById(id)
                        .map(personMapper::toResponseDto);
            } catch (Exception e) {
                log.error("Erro ao buscar pessoa por ID {}: {}", id, e.getMessage(), e);
                throw new RuntimeException("Erro ao buscar pessoa: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<Optional<PersonResponseDto>> findPersonByCpf(String cpf) {
        log.debug("Iniciando busca de pessoa por CPF de forma assíncrona: {}", cpf);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                return personService.findPersonByCpf(cpf)
                        .map(personMapper::toResponseDto);
            } catch (Exception e) {
                log.error("Erro ao buscar pessoa por CPF {}: {}", cpf, e.getMessage(), e);
                throw new RuntimeException("Erro ao buscar pessoa: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<List<PersonResponseDto>> listAllPersons() {
        log.debug("Iniciando listagem de todas as pessoas de forma assíncrona");
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Person> persons = personService.listAllPersons();
                return persons.stream()
                        .map(personMapper::toResponseDto)
                        .toList();
            } catch (Exception e) {
                log.error("Erro ao listar pessoas: {}", e.getMessage(), e);
                throw new RuntimeException("Erro ao listar pessoas: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<List<PersonResponseDto>> searchPersonsByName(String name) {
        log.debug("Iniciando busca de pessoas por nome de forma assíncrona: {}", name);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Person> persons = personService.searchPersonsByName(name);
                return persons.stream()
                        .map(personMapper::toResponseDto)
                        .toList();
            } catch (Exception e) {
                log.error("Erro ao buscar pessoas por nome {}: {}", name, e.getMessage(), e);
                throw new RuntimeException("Erro ao buscar pessoas: " + e.getMessage(), e);
            }
        });
    }

    @Override
    @Transactional
    public CompletableFuture<Void> deletePerson(Long id) {
        log.debug("Iniciando remoção de pessoa de forma assíncrona. ID: {}", id);
        
        return CompletableFuture.runAsync(() -> {
            try {
                personService.deletePerson(id);
            } catch (Exception e) {
                log.error("Erro ao remover pessoa com ID {}: {}", id, e.getMessage(), e);
                throw new RuntimeException("Erro ao remover pessoa: " + e.getMessage(), e);
            }
        });
    }

    @Override
    public CompletableFuture<Long> countPersons() {
        log.debug("Iniciando contagem de pessoas de forma assíncrona");
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Esta operação seria implementada no repositório se necessário
                return (long) personService.listAllPersons().size();
            } catch (Exception e) {
                log.error("Erro ao contar pessoas: {}", e.getMessage(), e);
                throw new RuntimeException("Erro ao contar pessoas: " + e.getMessage(), e);
            }
        });
    }
}
