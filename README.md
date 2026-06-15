# SSE - Sensores de Temperatura

Aplicação Spring Boot que simula leituras de sensores de temperatura e envia as atualizações para uma página HTML usando Server-Sent Events (SSE).

## Discente
Pedro Henrique de Araujo Pereira

## Tecnologias usadas

- Java 17
- Spring Boot
- Spring Web MVC
- SseEmitter
- HTML, CSS e JavaScript puro

## Como executar

Na pasta do projeto, execute:

```bash
mvn spring-boot:run
```

Depois acesse no navegador:

```text
http://localhost:8080/
```

A página exibirá três cards atualizados em tempo real: `sala`, `server` e `externo`.

## Endpoints

### Página principal

```http
GET /
```

Abre o front-end com os cards dos sensores.

### Stream SSE

```http
GET /sensores/stream
Accept: text/event-stream
```

Mantém uma conexão HTTP aberta e envia eventos do tipo `temperatura`.

Exemplo de evento enviado pelo servidor:

```text
id: 1
event: temperatura
data: {"sensor":"sala","valor":23.4,"timestamp":1710000000000}
```

### Status simples da aplicação

```http
GET /sensores/status
```

Retorna informações básicas da aplicação e a quantidade de conexões SSE ativas.

## Estrutura principal

```text
src/main/java/br/edu/puc/sse
├── SseSensoresTemperaturaApplication.java
├── config/AsyncConfig.java
├── controller/SensorController.java
├── model/LeituraTemperatura.java
├── service/EventoService.java
└── simulador/SensorSimulador.java
```

## Como funciona

1. O navegador abre uma conexão com `/sensores/stream` usando `EventSource`.
2. O servidor registra essa conexão usando `SseEmitter`.
3. A classe `SensorSimulador` gera leituras aleatórias a cada 2 segundos.
4. O `EventoService` publica as leituras de forma assíncrona para todos os clientes conectados.
5. O JavaScript recebe os eventos e atualiza os cards da página sem recarregar o navegador.

