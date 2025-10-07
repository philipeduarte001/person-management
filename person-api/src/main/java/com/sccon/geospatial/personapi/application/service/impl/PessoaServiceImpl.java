package com.sccon.geospatial.personapi.application.service.impl;

import com.sccon.geospatial.personapi.application.service.PessoaService;
import com.sccon.geospatial.personapi.domain.model.Pessoa;
import com.sccon.geospatial.personapi.domain.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;

    private static final double SALARIO_INICIAL = 1558.00;
    private static final double AUMENTO_PORCENTAGEM = 0.18;
    private static final double AUMENTO_FIXO = 500.00;
    private static final double SALARIO_MINIMO = 1302.00;

    public PessoaServiceImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Override
    public List<Pessoa> listarTodasOrdenadasPorNome() {
        log.debug("Listando todas as pessoas ordenadas por nome");
        return pessoaRepository.findAllOrderByNome();
    }

    @Override
    public Optional<Pessoa> buscarPorId(Long id) {
        log.debug("Buscando pessoa por ID: {}", id);
        return pessoaRepository.findById(id);
    }

    @Override
    public Pessoa criarPessoa(Pessoa pessoa) {
        log.debug("Criando nova pessoa: {}", pessoa.getNome());
        
        if (pessoa.getId() != null && pessoaRepository.existsById(pessoa.getId())) {
            throw new IllegalStateException("Pessoa com ID " + pessoa.getId() + " já existe");
        }
        
        return pessoaRepository.save(pessoa);
    }

    @Override
    public void removerPessoa(Long id) {
        log.debug("Removendo pessoa com ID: {}", id);
        
        if (!pessoaRepository.existsById(id)) {
            throw new IllegalStateException("Pessoa com ID " + id + " não encontrada");
        }
        
        pessoaRepository.deleteById(id);
    }

    @Override
    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) {
        log.debug("Atualizando pessoa com ID: {}", id);
        
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Pessoa com ID " + id + " não encontrada"));
        
        pessoa.setId(id);
        return pessoaRepository.save(pessoa);
    }

    @Override
    public Pessoa atualizarAtributoPessoa(Long id, String atributo, Object valor) {
        log.debug("Atualizando atributo '{}' da pessoa com ID: {}", atributo, id);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Pessoa com ID " + id + " não encontrada"));
        
        switch (atributo.toLowerCase()) {
            case "nome":
                pessoa.setNome((String) valor);
                break;
            case "datanascimento":
                pessoa.setDataNascimento((LocalDate) valor);
                break;
            case "dataadmissao":
                pessoa.setDataAdmissao((LocalDate) valor);
                break;
            default:
                throw new IllegalArgumentException("Atributo '" + atributo + "' não é válido");
        }
        
        return pessoaRepository.save(pessoa);
    }

    @Override
    public long calcularIdade(Long id, String formato) {
        log.debug("Calculando idade da pessoa com ID: {} no formato: {}", id, formato);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Pessoa com ID " + id + " não encontrada"));
        
        LocalDate hoje = LocalDate.now();
        LocalDate dataNascimento = pessoa.getDataNascimento();
        
        switch (formato.toLowerCase()) {
            case "days":
                return ChronoUnit.DAYS.between(dataNascimento, hoje);
            case "months":
                return ChronoUnit.MONTHS.between(dataNascimento, hoje);
            case "years":
                return ChronoUnit.YEARS.between(dataNascimento, hoje);
            default:
                throw new IllegalArgumentException("Formato '" + formato + "' não é válido. Use: days, months ou years");
        }
    }

    @Override
    public double calcularSalario(Long id, String formato) {
        log.debug("Calculando salário da pessoa com ID: {} no formato: {}", id, formato);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Pessoa com ID " + id + " não encontrada"));

        LocalDate hoje = LocalDate.now();
        LocalDate dataAdmissao = pessoa.getDataAdmissao();
        long anosEmpresa = ChronoUnit.YEARS.between(dataAdmissao, hoje);

        double salario = SALARIO_INICIAL;
        for (int i = 0; i < anosEmpresa; i++) {
            salario = salario + (salario * AUMENTO_PORCENTAGEM) + AUMENTO_FIXO;
        }
        
        switch (formato.toLowerCase()) {
            case "full":
                return Math.round(salario * 100.0) / 100.0;
            case "min":
                double salariosMinimos = salario / SALARIO_MINIMO;
                return Math.ceil(salariosMinimos * 100.0) / 100.0;
            default:
                throw new IllegalArgumentException("Formato '" + formato + "' não é válido. Use: full ou min");
        }
    }
}
