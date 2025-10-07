package com.sccon.geospatial.personapi.application.mapper;

import com.sccon.geospatial.personapi.application.dto.PersonRequestDto;
import com.sccon.geospatial.personapi.application.dto.PersonResponseDto;
import com.sccon.geospatial.personapi.domain.model.Person;
import org.springframework.stereotype.Component;


@Component
public class PersonMapper {

    public Person toEntity(PersonRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Person person = new Person();
        person.setName(dto.getName());
        person.setCpf(dto.getCpf());
        person.setPhone(dto.getPhone());
        person.setEmail(dto.getEmail());
        
        return person;
    }

    public PersonResponseDto toResponseDto(Person person) {
        if (person == null) {
            return null;
        }
        
        PersonResponseDto dto = new PersonResponseDto();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setCpf(person.getCpf());
        dto.setPhone(person.getPhone());
        dto.setEmail(person.getEmail());
        dto.setCreatedAt(person.getCreatedAt());
        dto.setUpdatedAt(person.getUpdatedAt());
        
        return dto;
    }
}
