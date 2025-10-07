package com.sccon.geospatial.personapi.infrastructure.repository;

import com.sccon.geospatial.personapi.domain.model.Pessoa;
import com.sccon.geospatial.personapi.domain.repository.PessoaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
public class InMemoryPessoaRepository implements PessoaRepository {

    private final Map<Long, Pessoa> pessoaMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Pessoa save(Pessoa pessoa) {
        log.debug("Salvando pessoa no mapa em memória: {}", pessoa.getNome());
        
        if (pessoa.getId() == null) {
            pessoa.setId(getNextId());
        }
        
        pessoaMap.put(pessoa.getId(), pessoa);
        log.info("Pessoa salva no mapa com ID: {}", pessoa.getId());
        
        return pessoa;
    }

    @Override
    public Optional<Pessoa> findById(Long id) {
        log.debug("Buscando pessoa por ID no mapa: {}", id);
        Pessoa pessoa = pessoaMap.get(id);
        return Optional.ofNullable(pessoa);
    }

    @Override
    public List<Pessoa> findAllOrderByNome() {
        log.debug("Buscando todas as pessoas ordenadas por nome");
        return pessoaMap.values().stream()
                .sorted(Comparator.comparing(Pessoa::getNome))
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        log.debug("Verificando existência de pessoa por ID no mapa: {}", id);
        return pessoaMap.containsKey(id);
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Removendo pessoa por ID do mapa: {}", id);
        Pessoa removed = pessoaMap.remove(id);
        if (removed != null) {
            log.info("Pessoa removida do mapa com ID: {}", id);
        } else {
            log.warn("Pessoa não encontrada para remoção com ID: {}", id);
        }
    }

    @Override
    public long count() {
        log.debug("Contando total de pessoas no mapa");
        return pessoaMap.size();
    }

    @Override
    public Long getNextId() {
        Long maxId = pessoaMap.keySet().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
        
        Long nextId = maxId + 1;
        idGenerator.set(nextId + 1);
        
        log.debug("Próximo ID disponível: {}", nextId);
        return nextId;
    }

    public Map<Long, Pessoa> getPessoaMap() {
        return new HashMap<>(pessoaMap);
    }
}
