package com.sccon.geospatial.personapi.domain.service.impl;

import com.sccon.geospatial.personapi.domain.model.Person;
import com.sccon.geospatial.personapi.domain.repository.PersonRepository;
import com.sccon.geospatial.personapi.domain.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public Person createPerson(Person person) {
        log.debug("Criando nova pessoa: {}", person.getName());
        
        validatePerson(person);
        
        if (personRepository.existsByCpf(person.getCpf())) {
            throw new IllegalStateException("Já existe uma pessoa cadastrada com o CPF: " + person.getCpf());
        }
        
        Person savedPerson = personRepository.save(person);
        log.info("Pessoa criada com sucesso. ID: {}, Nome: {}", savedPerson.getId(), savedPerson.getName());
        
        return savedPerson;
    }

    @Override
    @Transactional
    public Person updatePerson(Long id, Person person) {
        log.debug("Atualizando pessoa com ID: {}", id);
        
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Pessoa não encontrada com ID: " + id));
        
        validatePerson(person);

        if (!existingPerson.getCpf().equals(person.getCpf()) && 
            personRepository.existsByCpf(person.getCpf())) {
            throw new IllegalStateException("Já existe uma pessoa cadastrada com o CPF: " + person.getCpf());
        }

        existingPerson.setName(person.getName());
        existingPerson.setCpf(person.getCpf());
        existingPerson.setPhone(person.getPhone());
        existingPerson.setEmail(person.getEmail());
        
        Person updatedPerson = personRepository.save(existingPerson);
        log.info("Pessoa atualizada com sucesso. ID: {}, Nome: {}", updatedPerson.getId(), updatedPerson.getName());
        
        return updatedPerson;
    }

    @Override
    public Optional<Person> findPersonById(Long id) {
        log.debug("Buscando pessoa por ID: {}", id);
        return personRepository.findById(id);
    }

    @Override
    public Optional<Person> findPersonByCpf(String cpf) {
        log.debug("Buscando pessoa por CPF: {}", cpf);
        return personRepository.findByCpf(cpf);
    }

    @Override
    public List<Person> listAllPersons() {
        log.debug("Listando todas as pessoas");
        return personRepository.findAll();
    }

    @Override
    public List<Person> searchPersonsByName(String name) {
        log.debug("Buscando pessoas por nome: {}", name);
        return personRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public void deletePerson(Long id) {
        log.debug("Removendo pessoa com ID: {}", id);
        
        if (!personRepository.findById(id).isPresent()) {
            throw new IllegalStateException("Pessoa não encontrada com ID: " + id);
        }
        
        personRepository.deleteById(id);
        log.info("Pessoa removida com sucesso. ID: {}", id);
    }

    @Override
    public void validatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Pessoa não pode ser nula");
        }
        
        if (person.getName() == null || person.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (person.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        
        if (person.getName().trim().length() > 100) {
            throw new IllegalArgumentException("Nome deve ter no máximo 100 caracteres");
        }
        
        if (person.getCpf() == null || person.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (!person.isValidCpf()) {
            throw new IllegalArgumentException("CPF inválido");
        }
        
        if (person.getEmail() != null && !person.getEmail().trim().isEmpty()) {
            if (!isValidEmail(person.getEmail())) {
                throw new IllegalArgumentException("Email inválido");
            }
        }
        
        if (person.getPhone() != null && !person.getPhone().trim().isEmpty()) {
            if (!isValidPhone(person.getPhone())) {
                throw new IllegalArgumentException("Telefone inválido");
            }
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    private boolean isValidPhone(String phone) {
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        return cleanPhone.length() >= 10 && cleanPhone.length() <= 11;
    }
}
