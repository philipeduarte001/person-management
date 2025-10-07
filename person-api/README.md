# Person API Service

Uma API REST para gerenciamento de pessoas, desenvolvida seguindo os princípios SOLID, Clean Code, Arquitetura Hexagonal e utilizando Virtual Threads do Java 21+.

## 🏗️ Arquitetura

Esta aplicação segue a **Arquitetura Hexagonal (Ports & Adapters)**, organizando o código em camadas bem definidas:

```
src/main/java/com/sccon/geospatial/personapi/
├── domain/                    # Camada de Domínio
│   ├── model/                # Entidades de domínio
│   ├── repository/           # Interfaces de repositório (Ports)
│   └── service/              # Serviços de domínio
├── application/              # Camada de Aplicação
│   ├── dto/                  # Data Transfer Objects
│   ├── mapper/               # Mappers (MapStruct)
│   └── usecase/              # Casos de uso
└── infrastructure/           # Camada de Infraestrutura
    ├── config/               # Configurações
    ├── controller/           # Controllers REST (Adapters)
    ├── exception/            # Tratamento de exceções
    └── repository/           # Implementações de repositório
```

## 🎯 Princípios SOLID Aplicados

### S - Single Responsibility Principle (SRP)
- Cada classe tem uma única responsabilidade
- `PersonController` apenas expõe endpoints REST
- `PersonServiceImpl` apenas contém regras de negócio
- `PersonRepositoryImpl` apenas gerencia persistência

### O - Open/Closed Principle (OCP)
- Classes abertas para extensão, fechadas para modificação
- Uso de interfaces permite extensibilidade
- Adição de novos tipos de validação sem modificar código existente

### L - Liskov Substitution Principle (LSP)
- Implementações podem ser substituídas por suas abstrações
- `PersonRepositoryImpl` pode ser substituído por qualquer implementação de `PersonRepository`

### I - Interface Segregation Principle (ISP)
- Interfaces específicas e coesas
- `PersonService` e `PersonUseCase` têm responsabilidades bem definidas

### D - Dependency Inversion Principle (DIP)
- Dependências apontam para abstrações, não implementações
- Camada de domínio não depende de infraestrutura

## 🚀 Virtual Threads

A aplicação utiliza **Virtual Threads** do Java 21+ para otimizar o desempenho em operações assíncronas:

- Configuração automática no `PersonApiServiceApplication`
- Execução assíncrona de casos de uso com `CompletableFuture`
- Melhor escalabilidade para múltiplas chamadas de API

## 🛠️ Tecnologias Utilizadas

- **Java 25**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **H2 Database**
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Validation API**
- **JUnit 5** + **Mockito** (testes)

## 📋 Pré-requisitos

- Java 25 ou superior
- Maven 3.6+
- IDE compatível (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Como Executar

1. **Clone o repositório**
```bash
git clone <repository-url>
cd person-api
```

2. **Execute a aplicação**
```bash
./mvnw spring-boot:run
```

3. **Acesse a aplicação**
- API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Health Check: http://localhost:8080/api/v1/health

## 📚 Endpoints da API

### Pessoas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/persons` | Criar nova pessoa |
| GET | `/api/v1/persons` | Listar todas as pessoas |
| GET | `/api/v1/persons/{id}` | Buscar pessoa por ID |
| GET | `/api/v1/persons/cpf/{cpf}` | Buscar pessoa por CPF |
| GET | `/api/v1/persons/search?name={name}` | Buscar pessoas por nome |
| PUT | `/api/v1/persons/{id}` | Atualizar pessoa |
| DELETE | `/api/v1/persons/{id}` | Remover pessoa |
| GET | `/api/v1/persons/count` | Contar total de pessoas |

### Health Check

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/health` | Status da aplicação |
| GET | `/api/v1/health/ready` | Readiness check |

### Mapa em Memória

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/memory-map/stats` | Estatísticas do mapa em memória |
| GET | `/api/v1/memory-map/content` | Conteúdo completo do mapa |

## 📝 Exemplo de Uso

### Criar uma pessoa
```bash
curl -X POST http://localhost:8080/api/v1/persons \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "cpf": "123.456.789-00",
    "phone": "(11) 99999-9999",
    "email": "joao@email.com"
  }'
```

### Buscar pessoa por ID
```bash
curl http://localhost:8080/api/v1/persons/1
```

### Listar todas as pessoas
```bash
curl http://localhost:8080/api/v1/persons
```

### Verificar mapa em memória
```bash
# Estatísticas do mapa
curl http://localhost:8080/api/v1/memory-map/stats

# Conteúdo completo do mapa
curl http://localhost:8080/api/v1/memory-map/content
```

## 🧪 Executando Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes com cobertura
./mvnw test jacoco:report

# Executar testes de integração
./mvnw verify
```

## 📊 Monitoramento

A aplicação inclui endpoints de monitoramento via Spring Boot Actuator:

- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

## 🔧 Configurações

As configurações estão no arquivo `application.properties`:

- **Database**: H2 em memória
- **Virtual Threads**: Habilitado
- **Logging**: Nível DEBUG para desenvolvimento
- **CORS**: Configurado para desenvolvimento

## 📈 Benefícios da Arquitetura

1. **Testabilidade**: Fácil criação de testes unitários e de integração
2. **Manutenibilidade**: Código organizado e responsabilidades claras
3. **Escalabilidade**: Virtual Threads para melhor performance
4. **Flexibilidade**: Fácil troca de implementações (ex: banco de dados)
5. **Clean Code**: Código legível e bem documentado

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
