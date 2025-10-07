package com.sccon.geospatial.personapi.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Verificando saúde da aplicação");
        
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("application", "Person API Service");
        healthStatus.put("version", "1.0.0");
        healthStatus.put("javaVersion", System.getProperty("java.version"));
        healthStatus.put("virtualThreads", Thread.currentThread().isVirtual());
        
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/ready")
    public ResponseEntity<Map<String, Object>> readiness() {
        log.debug("Verificando readiness da aplicação");
        
        Map<String, Object> readinessStatus = new HashMap<>();
        readinessStatus.put("status", "READY");
        readinessStatus.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(readinessStatus);
    }
}
