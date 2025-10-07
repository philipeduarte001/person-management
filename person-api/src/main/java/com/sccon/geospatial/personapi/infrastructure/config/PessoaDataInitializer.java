package com.sccon.geospatial.personapi.infrastructure.config;

import com.sccon.geospatial.personapi.domain.model.Pessoa;
import com.sccon.geospatial.personapi.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class PessoaDataInitializer implements CommandLineRunner {

    private final PessoaRepository pessoaRepository;

    public PessoaDataInitializer(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando população do mapa de pessoas conforme especificação...");
        
        initializePessoasData();
        
        log.info("População do mapa de pessoas concluída com sucesso!");
    }

    private void initializePessoasData() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("José da Silva");
        pessoa1.setDataNascimento(LocalDate.of(2000, 4, 6)); // 06/04/2000
        pessoa1.setDataAdmissao(LocalDate.of(2020, 5, 10));  // 10/05/2020

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("Maria Santos");
        pessoa2.setDataNascimento(LocalDate.of(1995, 8, 15));
        pessoa2.setDataAdmissao(LocalDate.of(2021, 3, 20));

        Pessoa pessoa3 = new Pessoa();
        pessoa3.setId(3L);
        pessoa3.setNome("Pedro Oliveira");
        pessoa3.setDataNascimento(LocalDate.of(1988, 12, 3));
        pessoa3.setDataAdmissao(LocalDate.of(2019, 1, 15));

        pessoaRepository.save(pessoa1);
        pessoaRepository.save(pessoa2);
        pessoaRepository.save(pessoa3);

        log.info("✅ Mapa de pessoas inicializado conforme especificação!");
        log.info("📊 3 pessoas cadastradas:");
        log.info("👤 José da Silva (ID: {}) - Nascimento: {}, Admissão: {}", 
                pessoa1.getId(), pessoa1.getDataNascimento(), pessoa1.getDataAdmissao());
        log.info("👤 Maria Santos (ID: {}) - Nascimento: {}, Admissão: {}", 
                pessoa2.getId(), pessoa2.getDataNascimento(), pessoa2.getDataAdmissao());
        log.info("👤 Pedro Oliveira (ID: {}) - Nascimento: {}, Admissão: {}", 
                pessoa3.getId(), pessoa3.getDataNascimento(), pessoa3.getDataAdmissao());
        log.info("🗺️  Mapa em memória pronto para busca por ID!");
    }
}
