package com.sccon.geospatial.personapi.infrastructure.controller;

import com.sccon.geospatial.personapi.application.dto.PersonRequestDto;
import com.sccon.geospatial.personapi.application.dto.PersonResponseDto;
import com.sccon.geospatial.personapi.application.usecase.PersonUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    
    private final PersonUseCase personUseCase;
    
    @Autowired
    public PersonController(PersonUseCase personUseCase) {
        this.personUseCase = personUseCase;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<PersonResponseDto>> createPerson(
            @Valid @RequestBody PersonRequestDto requestDto) {
        
        log.info("Recebendo requisição para criar pessoa: {}", requestDto.getName());
        
        return personUseCase.createPerson(requestDto)
                .thenApply(ResponseEntity.status(HttpStatus.CREATED)::body)
                .exceptionally(throwable -> {
                    log.error("Erro ao criar pessoa: {}", throwable.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                });
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<PersonResponseDto>> updatePerson(
            @PathVariable Long id,
            @Valid @RequestBody PersonRequestDto requestDto) {
        
        log.info("Recebendo requisição para atualizar pessoa com ID: {}", id);
        
        return personUseCase.updatePerson(id, requestDto)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error("Erro ao atualizar pessoa com ID {}: {}", id, throwable.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                });
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<?>> getPersonById(@PathVariable Long id) {
        log.info("Recebendo requisição para buscar pessoa por ID: {}", id);
        
        return personUseCase.findPersonById(id)
                .thenApply(optionalPerson -> {
                    if (optionalPerson.isPresent()) {
                        return ResponseEntity.<PersonResponseDto>ok(optionalPerson.get());
                    } else {
                        return ResponseEntity.<PersonResponseDto>notFound().build();
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Erro ao buscar pessoa por ID {}: {}", id, throwable.getMessage());
                    return ResponseEntity.<PersonResponseDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @GetMapping("/cpf/{cpf}")
    public CompletableFuture<ResponseEntity<?>> getPersonByCpf(@PathVariable String cpf) {
        log.info("Recebendo requisição para buscar pessoa por CPF: {}", cpf);
        
        return personUseCase.findPersonByCpf(cpf)
                .thenApply(optionalPerson -> {
                    if (optionalPerson.isPresent()) {
                        return ResponseEntity.<PersonResponseDto>ok(optionalPerson.get());
                    } else {
                        return ResponseEntity.<PersonResponseDto>notFound().build();
                    }
                })
                .exceptionally(throwable -> {
                    log.error("Erro ao buscar pessoa por CPF {}: {}", cpf, throwable.getMessage());
                    return ResponseEntity.<PersonResponseDto>status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<PersonResponseDto>>> getAllPersons() {
        log.info("Recebendo requisição para listar todas as pessoas");
        
        return personUseCase.listAllPersons()
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error("Erro ao listar pessoas: {}", throwable.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @GetMapping("/search")
    public CompletableFuture<ResponseEntity<List<PersonResponseDto>>> searchPersonsByName(
            @RequestParam String name) {
        
        log.info("Recebendo requisição para buscar pessoas por nome: {}", name);
        
        return personUseCase.searchPersonsByName(name)
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error("Erro ao buscar pessoas por nome {}: {}", name, throwable.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deletePerson(@PathVariable Long id) {
        log.info("Recebendo requisição para remover pessoa com ID: {}", id);
        
        return personUseCase.deletePerson(id)
                .handle((unused, throwable) -> {
                    if (throwable != null) {
                        log.error("Erro ao remover pessoa com ID {}: {}", id, throwable.getMessage());
                        return ResponseEntity.<Void>status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    } else {
                        return ResponseEntity.<Void>noContent().build();
                    }
                });
    }

    @GetMapping("/count")
    public CompletableFuture<ResponseEntity<Long>> countPersons() {
        log.info("Recebendo requisição para contar pessoas");
        
        return personUseCase.countPersons()
                .thenApply(ResponseEntity::ok)
                .exceptionally(throwable -> {
                    log.error("Erro ao contar pessoas: {}", throwable.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}
