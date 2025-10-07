package com.sccon.geospatial.personapi.application.usecase;

import com.sccon.geospatial.personapi.application.dto.PersonRequestDto;
import com.sccon.geospatial.personapi.application.dto.PersonResponseDto;
import com.sccon.geospatial.personapi.application.mapper.PersonMapper;
import com.sccon.geospatial.personapi.application.usecase.impl.PersonUseCaseImpl;
import com.sccon.geospatial.personapi.domain.model.Person;
import com.sccon.geospatial.personapi.domain.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonUseCaseImplTest {

    @Mock
    private PersonService personService;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonUseCaseImpl personUseCase;

    private PersonRequestDto personRequestDto;
    private Person person;
    private PersonResponseDto personResponseDto;

    @BeforeEach
    void setUp() {
        personRequestDto = new PersonRequestDto();
        personRequestDto.setName("João Silva");
        personRequestDto.setCpf("123.456.789-00");
        personRequestDto.setPhone("(11) 99999-9999");
        personRequestDto.setEmail("joao@email.com");

        person = new Person();
        person.setId(1L);
        person.setName("João Silva");
        person.setCpf("123.456.789-00");
        person.setPhone("(11) 99999-9999");
        person.setEmail("joao@email.com");
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());

        personResponseDto = new PersonResponseDto();
        personResponseDto.setId(1L);
        personResponseDto.setName("João Silva");
        personResponseDto.setCpf("123.456.789-00");
        personResponseDto.setPhone("(11) 99999-9999");
        personResponseDto.setEmail("joao@email.com");
        personResponseDto.setCreatedAt(LocalDateTime.now());
        personResponseDto.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createPerson_ShouldReturnPersonResponseDto_WhenValidData() throws Exception {

        when(personMapper.toEntity(personRequestDto)).thenReturn(person);
        when(personService.createPerson(person)).thenReturn(person);
        when(personMapper.toResponseDto(person)).thenReturn(personResponseDto);

        CompletableFuture<PersonResponseDto> result = personUseCase.createPerson(personRequestDto);
        PersonResponseDto response = result.get();

        assertNotNull(response);
        assertEquals(personResponseDto.getId(), response.getId());
        assertEquals(personResponseDto.getName(), response.getName());
        assertEquals(personResponseDto.getCpf(), response.getCpf());

        verify(personMapper).toEntity(personRequestDto);
        verify(personService).createPerson(person);
        verify(personMapper).toResponseDto(person);
    }

    @Test
    void findPersonById_ShouldReturnPersonResponseDto_WhenPersonExists() throws Exception {

        Long personId = 1L;
        when(personService.findPersonById(personId)).thenReturn(Optional.of(person));
        when(personMapper.toResponseDto(person)).thenReturn(personResponseDto);

        CompletableFuture<Optional<PersonResponseDto>> result = personUseCase.findPersonById(personId);
        Optional<PersonResponseDto> response = result.get();

        assertTrue(response.isPresent());
        assertEquals(personResponseDto.getId(), response.get().getId());

        verify(personService).findPersonById(personId);
        verify(personMapper).toResponseDto(person);
    }

    @Test
    void findPersonById_ShouldReturnEmptyOptional_WhenPersonDoesNotExist() throws Exception {

        Long personId = 999L;
        when(personService.findPersonById(personId)).thenReturn(Optional.empty());

        CompletableFuture<Optional<PersonResponseDto>> result = personUseCase.findPersonById(personId);
        Optional<PersonResponseDto> response = result.get();

        assertFalse(response.isPresent());
        verify(personService).findPersonById(personId);
        verify(personMapper, never()).toResponseDto(any());
    }

    @Test
    void listAllPersons_ShouldReturnListOfPersons() throws Exception {

        List<Person> persons = Arrays.asList(person);
        List<PersonResponseDto> responseDtos = Arrays.asList(personResponseDto);

        when(personService.listAllPersons()).thenReturn(persons);
        when(personMapper.toResponseDto(person)).thenReturn(personResponseDto);

        CompletableFuture<List<PersonResponseDto>> result = personUseCase.listAllPersons();
        List<PersonResponseDto> response = result.get();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(personResponseDto.getId(), response.get(0).getId());

        verify(personService).listAllPersons();
        verify(personMapper).toResponseDto(person);
    }

    @Test
    void deletePerson_ShouldCompleteSuccessfully_WhenPersonExists() throws Exception {

        Long personId = 1L;
        doNothing().when(personService).deletePerson(personId);

        CompletableFuture<Void> result = personUseCase.deletePerson(personId);
        result.get();

        verify(personService).deletePerson(personId);
    }

    @Test
    void updatePerson_ShouldReturnUpdatedPersonResponseDto_WhenValidData() throws Exception {

        Long personId = 1L;
        Person updatedPerson = new Person();
        updatedPerson.setId(1L);
        updatedPerson.setName("João Silva Atualizado");
        updatedPerson.setCpf("123.456.789-00");
        updatedPerson.setPhone("(11) 99999-9999");
        updatedPerson.setEmail("joao@email.com");
        updatedPerson.setCreatedAt(LocalDateTime.now());
        updatedPerson.setUpdatedAt(LocalDateTime.now());

        PersonResponseDto updatedResponseDto = new PersonResponseDto();
        updatedResponseDto.setId(1L);
        updatedResponseDto.setName("João Silva Atualizado");
        updatedResponseDto.setCpf("123.456.789-00");
        updatedResponseDto.setPhone("(11) 99999-9999");
        updatedResponseDto.setEmail("joao@email.com");
        updatedResponseDto.setCreatedAt(LocalDateTime.now());
        updatedResponseDto.setUpdatedAt(LocalDateTime.now());

        when(personMapper.toEntity(personRequestDto)).thenReturn(person);
        when(personService.updatePerson(personId, person)).thenReturn(updatedPerson);
        when(personMapper.toResponseDto(updatedPerson)).thenReturn(updatedResponseDto);

        CompletableFuture<PersonResponseDto> result = personUseCase.updatePerson(personId, personRequestDto);
        PersonResponseDto response = result.get();

        assertNotNull(response);
        assertEquals(updatedResponseDto.getName(), response.getName());

        verify(personMapper).toEntity(personRequestDto);
        verify(personService).updatePerson(personId, person);
        verify(personMapper).toResponseDto(updatedPerson);
    }
}
