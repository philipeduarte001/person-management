package com.sccon.geospatial.personapi.infrastructure.controller;

import com.sccon.geospatial.personapi.application.dto.PessoaRequestDto;
import com.sccon.geospatial.personapi.application.dto.PessoaResponseDto;
import com.sccon.geospatial.personapi.application.mapper.PessoaMapper;
import com.sccon.geospatial.personapi.application.service.PessoaService;
import com.sccon.geospatial.personapi.domain.model.Pessoa;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/person")
public class PessoaController {

    private static final Logger log = LoggerFactory.getLogger(PessoaController.class);
    
    private final PessoaService pessoaService;
    private final PessoaMapper pessoaMapper;
    
    @Autowired
    public PessoaController(PessoaService pessoaService, PessoaMapper pessoaMapper) {
        this.pessoaService = pessoaService;
        this.pessoaMapper = pessoaMapper;
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<PessoaResponseDto>>> listarTodasPessoas() {
        log.info("Recebendo requisição GET /person");
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Pessoa> pessoas = pessoaService.listarTodasOrdenadasPorNome();
                List<PessoaResponseDto> responseDtos = pessoas.stream()
                        .map(pessoaMapper::toResponseDto)
                        .toList();
                
                return ResponseEntity.ok(responseDtos);
            } catch (Exception e) {
                log.error("Erro ao listar pessoas: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<PessoaResponseDto>> buscarPessoaPorId(@PathVariable Long id) {
        log.info("Recebendo requisição GET /person/{}", id);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Pessoa> pessoa = pessoaService.buscarPorId(id);
                if (pessoa.isPresent()) {
                    PessoaResponseDto responseDto = pessoaMapper.toResponseDto(pessoa.get());
                    return ResponseEntity.ok(responseDto);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                log.error("Erro ao buscar pessoa por ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<PessoaResponseDto>> criarPessoa(@Valid @RequestBody PessoaRequestDto requestDto) {
        log.info("Recebendo requisição POST /person para pessoa: {}", requestDto.getNome());
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Pessoa pessoa = pessoaMapper.toEntity(requestDto);
                Pessoa pessoaCriada = pessoaService.criarPessoa(pessoa);
                PessoaResponseDto responseDto = pessoaMapper.toResponseDto(pessoaCriada);
                
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            } catch (IllegalStateException e) {
                log.warn("Conflito ao criar pessoa: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } catch (Exception e) {
                log.error("Erro ao criar pessoa: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        });
    }

    /**
     * DELETE /person/{id} - Remove pessoa por ID
     */
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> removerPessoa(@PathVariable Long id) {
        log.info("Recebendo requisição DELETE /person/{}", id);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                pessoaService.removerPessoa(id);
                return ResponseEntity.noContent().build();
            } catch (IllegalStateException e) {
                log.warn("Pessoa não encontrada para remoção: {}", e.getMessage());
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                log.error("Erro ao remover pessoa com ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<PessoaResponseDto>> atualizarPessoa(
            @PathVariable Long id, 
            @Valid @RequestBody PessoaRequestDto requestDto) {
        
        log.info("Recebendo requisição PUT /person/{}", id);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Pessoa pessoa = pessoaMapper.toEntity(requestDto);
                Pessoa pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoa);
                PessoaResponseDto responseDto = pessoaMapper.toResponseDto(pessoaAtualizada);
                
                return ResponseEntity.ok(responseDto);
            } catch (IllegalStateException e) {
                log.warn("Pessoa não encontrada para atualização: {}", e.getMessage());
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                log.error("Erro ao atualizar pessoa com ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        });
    }

    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<PessoaResponseDto>> atualizarAtributoPessoa(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> updates) {
        
        log.info("Recebendo requisição PATCH /person/{}", id);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                Pessoa pessoaAtualizada = null;
                
                for (Map.Entry<String, Object> entry : updates.entrySet()) {
                    pessoaAtualizada = pessoaService.atualizarAtributoPessoa(id, entry.getKey(), entry.getValue());
                }
                
                if (pessoaAtualizada != null) {
                    PessoaResponseDto responseDto = pessoaMapper.toResponseDto(pessoaAtualizada);
                    return ResponseEntity.ok(responseDto);
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } catch (IllegalStateException e) {
                log.warn("Pessoa não encontrada para atualização: {}", e.getMessage());
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                log.error("Erro ao atualizar pessoa com ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        });
    }

    @GetMapping("/{id}/age")
    public CompletableFuture<ResponseEntity<Long>> calcularIdade(
            @PathVariable Long id, 
            @RequestParam String output) {
        
        log.info("Recebendo requisição GET /person/{}/age?output={}", id, output);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                long idade = pessoaService.calcularIdade(id, output);
                return ResponseEntity.ok(idade);
            } catch (IllegalStateException e) {
                log.warn("Pessoa não encontrada: {}", e.getMessage());
                return ResponseEntity.notFound().build();
            } catch (IllegalArgumentException e) {
                log.warn("Formato inválido: {}", e.getMessage());
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                log.error("Erro ao calcular idade da pessoa com ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @GetMapping("/{id}/salary")
    public CompletableFuture<ResponseEntity<Double>> calcularSalario(
            @PathVariable Long id, 
            @RequestParam String output) {
        
        log.info("Recebendo requisição GET /person/{}/salary?output={}", id, output);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                double salario = pessoaService.calcularSalario(id, output);
                return ResponseEntity.ok(salario);
            } catch (IllegalStateException e) {
                log.warn("Pessoa não encontrada: {}", e.getMessage());
                return ResponseEntity.notFound().build();
            } catch (IllegalArgumentException e) {
                log.warn("Formato inválido: {}", e.getMessage());
                return ResponseEntity.badRequest().build();
            } catch (Exception e) {
                log.error("Erro ao calcular salário da pessoa com ID {}: {}", id, e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }
}
