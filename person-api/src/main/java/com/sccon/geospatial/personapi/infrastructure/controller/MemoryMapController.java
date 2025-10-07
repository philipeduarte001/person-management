package com.sccon.geospatial.personapi.infrastructure.controller;

import com.sccon.geospatial.personapi.infrastructure.repository.InMemoryPersonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller para mostrar informações do mapa em memória
 * Seguindo o princípio da responsabilidade única (SRP)
 */
@RestController
@RequestMapping("/api/v1/memory-map")
public class MemoryMapController {

    private static final Logger log = LoggerFactory.getLogger(MemoryMapController.class);
    
    private final InMemoryPersonRepository inMemoryPersonRepository;

    public MemoryMapController(InMemoryPersonRepository inMemoryPersonRepository) {
        this.inMemoryPersonRepository = inMemoryPersonRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMemoryMapStats() {
        log.info("Solicitando estatísticas do mapa em memória");
        
        Map<String, Object> stats = inMemoryPersonRepository.getStatistics();
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/content")
    public ResponseEntity<Map<Long, com.sccon.geospatial.personapi.domain.model.Person>> getMemoryMapContent() {
        log.info("Solicitando conteúdo completo do mapa em memória");
        
        Map<Long, com.sccon.geospatial.personapi.domain.model.Person> personMap = 
                inMemoryPersonRepository.getPersonMap();
        
        return ResponseEntity.ok(personMap);
    }
}
