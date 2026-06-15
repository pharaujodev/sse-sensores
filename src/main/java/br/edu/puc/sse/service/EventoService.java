package br.edu.puc.sse.service;

import br.edu.puc.sse.model.LeituraTemperatura;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EventoService {

    private static final long SEM_TIMEOUT = -1L;
    private static final long RECONEXAO_EM_MILISSEGUNDOS = 3000L;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final AtomicLong contadorEventos = new AtomicLong(0);

    public SseEmitter criarConexao(String ultimoEventoRecebido) {
        SseEmitter emitter = new SseEmitter(SEM_TIMEOUT);

        emitter.onCompletion(() -> remover(emitter));
        emitter.onTimeout(() -> remover(emitter));
        emitter.onError(erro -> remover(emitter));

        registrar(emitter);
        enviarBoasVindas(emitter, ultimoEventoRecebido);

        return emitter;
    }

    public void registrar(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void remover(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    @Async
    public void publicar(LeituraTemperatura leitura) {
        String id = String.valueOf(contadorEventos.incrementAndGet());

        SseEmitter.SseEventBuilder evento = SseEmitter.event()
                .id(id)
                .name("temperatura")
                .data(leitura)
                .reconnectTime(RECONEXAO_EM_MILISSEGUNDOS);

        enviarParaTodos(evento);
    }

    @Scheduled(fixedRate = 20000)
    public void enviarHeartbeat() {
        SseEmitter.SseEventBuilder heartbeat = SseEmitter.event()
                .comment("heartbeat");

        enviarParaTodos(heartbeat);
    }

    public int totalConexoesAtivas() {
        return emitters.size();
    }

    private void enviarBoasVindas(SseEmitter emitter, String ultimoEventoRecebido) {
        String mensagem = ultimoEventoRecebido == null
                ? "conectado"
                : "reconectado a partir do evento " + ultimoEventoRecebido;

        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(contadorEventos.incrementAndGet()))
                    .name("status")
                    .data(mensagem)
                    .reconnectTime(RECONEXAO_EM_MILISSEGUNDOS));
        } catch (IOException e) {
            remover(emitter);
        }
    }

    private void enviarParaTodos(SseEmitter.SseEventBuilder evento) {
        List<SseEmitter> desconectados = new ArrayList<>();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(evento);
            } catch (Exception e) {
                desconectados.add(emitter);
            }
        }

        emitters.removeAll(desconectados);
    }
}
