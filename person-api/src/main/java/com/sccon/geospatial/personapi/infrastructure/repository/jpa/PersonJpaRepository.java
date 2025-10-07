package com.sccon.geospatial.personapi.infrastructure.repository.jpa;

import com.sccon.geospatial.personapi.domain.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByCpf(String cpf);

    List<Person> findByNameContainingIgnoreCase(String name);

    boolean existsByCpf(String cpf);

    @Query("SELECT p FROM Person p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Person> findByCustomNameSearch(@Param("name") String name);
}
