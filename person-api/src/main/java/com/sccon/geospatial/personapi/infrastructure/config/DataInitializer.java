package com.sccon.geospatial.personapi.infrastructure.config;

import com.sccon.geospatial.personapi.domain.model.Person;
import com.sccon.geospatial.personapi.domain.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Inicializador de dados para popular o mapa de pessoas em memória
 * Seguindo o princípio da responsabilidade única (SRP)
 */
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PersonRepository personRepository;

    public DataInitializer(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando população do mapa de pessoas em memória...");
        
        initializePeopleData();
        
        log.info("População do mapa de pessoas concluída com sucesso!");
    }

    private void initializePeopleData() {

        Person person1 = new Person();
        person1.setName("João Silva");
        person1.setCpf("123.456.789-00");
        person1.setPhone("(11) 99999-1111");
        person1.setEmail("joao.silva@email.com");

        Person person2 = new Person();
        person2.setName("Maria Santos");
        person2.setCpf("987.654.321-00");
        person2.setPhone("(21) 88888-2222");
        person2.setEmail("maria.santos@email.com");

        Person person3 = new Person();
        person3.setName("Pedro Oliveira");
        person3.setCpf("456.789.123-00");
        person3.setPhone("(31) 77777-3333");
        person3.setEmail("pedro.oliveira@email.com");

        Person savedPerson1 = personRepository.save(person1);
        Person savedPerson2 = personRepository.save(person2);
        Person savedPerson3 = personRepository.save(person3);

        log.info("✅ Mapa de pessoas inicializado com sucesso!");
        log.info("📊 3 pessoas cadastradas com IDs: {}, {}, {}", 
                savedPerson1.getId(), savedPerson2.getId(), savedPerson3.getId());
        log.info("👤 João Silva (ID: {})", savedPerson1.getId());
        log.info("👤 Maria Santos (ID: {})", savedPerson2.getId());
        log.info("👤 Pedro Oliveira (ID: {})", savedPerson3.getId());
        log.info("🗺️  Mapa em memória pronto para busca por ID!");
    }
}
