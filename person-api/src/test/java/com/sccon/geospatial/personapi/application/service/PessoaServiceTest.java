package com.sccon.geospatial.personapi.application.service;

import com.sccon.geospatial.personapi.application.service.impl.PessoaServiceImpl;
import com.sccon.geospatial.personapi.domain.model.Pessoa;
import com.sccon.geospatial.personapi.domain.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaServiceImpl pessoaService;

    private Pessoa joseSilva;

    @BeforeEach
    void setUp() {
        // Pessoa do exemplo da especificação
        joseSilva = new Pessoa();
        joseSilva.setId(1L);
        joseSilva.setNome("José da Silva");
        joseSilva.setDataNascimento(LocalDate.of(2000, 4, 6)); // 06/04/2000
        joseSilva.setDataAdmissao(LocalDate.of(2020, 5, 10));  // 10/05/2020
    }

    @Test
    void calcularIdade_DeveRetornarValoresCorretos_ConformeEspecificacao() {

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(joseSilva));

        long idadeAnos = pessoaService.calcularIdade(1L, "years");
        assertEquals(25, idadeAnos);

        long idadeMeses = pessoaService.calcularIdade(1L, "months");
        assertEquals(306, idadeMeses);

        long idadeDias = pessoaService.calcularIdade(1L, "days");
        assertEquals(9315, idadeDias);
    }

    @Test
    void calcularSalario_DeveRetornarValoresCorretos_ConformeEspecificacao() {
        // Arrange
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(joseSilva));

        double salarioFull = pessoaService.calcularSalario(1L, "full");
        assertEquals(7141.43, salarioFull, 0.01);

        double salarioMin = pessoaService.calcularSalario(1L, "min");
        assertEquals(5.49, salarioMin, 0.01);
    }

    @Test
    void calcularIdade_ComFormatoInvalido_DeveLancarExcecao() {

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(joseSilva));

        assertThrows(IllegalArgumentException.class, () -> {
            pessoaService.calcularIdade(1L, "invalid");
        });
    }

    @Test
    void calcularSalario_ComFormatoInvalido_DeveLancarExcecao() {

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(joseSilva));

        assertThrows(IllegalArgumentException.class, () -> {
            pessoaService.calcularSalario(1L, "invalid");
        });
    }

    @Test
    void calcularIdade_PessoaNaoEncontrada_DeveLancarExcecao() {

        when(pessoaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> {
            pessoaService.calcularIdade(999L, "years");
        });
    }
}
