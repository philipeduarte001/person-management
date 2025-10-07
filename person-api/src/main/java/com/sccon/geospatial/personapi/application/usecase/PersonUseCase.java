package com.sccon.geospatial.personapi.application.usecase;

import com.sccon.geospatial.personapi.application.dto.PersonRequestDto;
import com.sccon.geospatial.personapi.application.dto.PersonResponseDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface PersonUseCase {

    CompletableFuture<PersonResponseDto> createPerson(PersonRequestDto requestDto);

    CompletableFuture<PersonResponseDto> updatePerson(Long id, PersonRequestDto requestDto);

    CompletableFuture<java.util.Optional<PersonResponseDto>> findPersonById(Long id);

    CompletableFuture<java.util.Optional<PersonResponseDto>> findPersonByCpf(String cpf);

    CompletableFuture<List<PersonResponseDto>> listAllPersons();

    CompletableFuture<List<PersonResponseDto>> searchPersonsByName(String name);

    CompletableFuture<Void> deletePerson(Long id);

    CompletableFuture<Long> countPersons();
}
