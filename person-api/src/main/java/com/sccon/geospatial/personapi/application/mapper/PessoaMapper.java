package com.sccon.geospatial.personapi.application.mapper;

import com.sccon.geospatial.personapi.application.dto.PessoaRequestDto;
import com.sccon.geospatial.personapi.application.dto.PessoaResponseDto;
import com.sccon.geospatial.personapi.domain.model.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {

    public Pessoa toEntity(PessoaRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        Pessoa pessoa = new Pessoa();
        pessoa.setId(dto.getId());
        pessoa.setNome(dto.getNome());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setDataAdmissao(dto.getDataAdmissao());
        
        return pessoa;
    }

    public PessoaResponseDto toResponseDto(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }
        
        PessoaResponseDto dto = new PessoaResponseDto();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setDataNascimento(pessoa.getDataNascimento());
        dto.setDataAdmissao(pessoa.getDataAdmissao());
        
        return dto;
    }
}
