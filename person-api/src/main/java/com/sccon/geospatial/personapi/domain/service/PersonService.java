package com.sccon.geospatial.personapi.domain.service;

import com.sccon.geospatial.personapi.domain.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person createPerson(Person person);

    Person updatePerson(Long id, Person person);

    Optional<Person> findPersonById(Long id);

    Optional<Person> findPersonByCpf(String cpf);

    List<Person> listAllPersons();

    List<Person> searchPersonsByName(String name);

    void deletePerson(Long id);

    void validatePerson(Person person);
}
