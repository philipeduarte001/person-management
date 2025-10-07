package com.sccon.geospatial.personapi.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sccon.geospatial.personapi.application.dto.PersonRequestDto;
import com.sccon.geospatial.personapi.application.usecase.PersonUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
@ExtendWith(MockitoExtension.class)
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static PersonUseCase mockPersonUseCase = org.mockito.Mockito.mock(PersonUseCase.class);

    @TestConfiguration
    static class TestConfig {
        
        @Bean
        @Primary
        public PersonUseCase personUseCase() {
            return mockPersonUseCase;
        }
    }

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(mockPersonUseCase);
    }

    @Test
    void createPerson_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        // Given
        PersonRequestDto requestDto = new PersonRequestDto();
        requestDto.setName(""); // Nome vazio - inválido
        requestDto.setCpf("123"); // CPF inválido

        // When & Then
        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllPersons_ShouldReturnOk_WhenCalled() throws Exception {
        // Given
        when(mockPersonUseCase.listAllPersons())
                .thenReturn(CompletableFuture.completedFuture(java.util.List.of()));

        // When & Then
        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk());
    }

    @Test
    void searchPersonsByName_ShouldReturnOk_WhenCalled() throws Exception {
        // Given
        String name = "João";
        when(mockPersonUseCase.searchPersonsByName(name))
                .thenReturn(CompletableFuture.completedFuture(java.util.List.of()));

        // When & Then
        mockMvc.perform(get("/api/v1/persons/search")
                        .param("name", name))
                .andExpect(status().isOk());
    }

}
