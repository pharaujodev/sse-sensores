package br.edu.puc.sse.controller;

import br.edu.puc.sse.service.EventoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@RestController
@RequestMapping("/sensores")
@CrossOrigin(origins = "*")
public class SensorController {

    private final EventoService eventoService;

    public SensorController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(
            @RequestHeader(value = "Last-Event-ID", required = false) String ultimoEventoRecebido) {
        return eventoService.criarConexao(ultimoEventoRecebido);
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "aplicacao", "sse-sensores-temperatura",
                "conexoesAtivas", eventoService.totalConexoesAtivas(),
                "endpointSse", "/sensores/stream"
        );
    }
}
