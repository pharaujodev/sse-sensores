package br.edu.puc.sse.simulador;

import br.edu.puc.sse.model.LeituraTemperatura;
import br.edu.puc.sse.service.EventoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SensorSimulador {

    private static final long INTERVALO_LEITURA_EM_MILISSEGUNDOS = 2000L;

    private final EventoService eventoService;
    private final Map<String, Double> temperaturasBase = Map.of(
            "sala", 23.0,
            "server", 31.5,
            "externo", 27.0
    );

    public SensorSimulador(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @Scheduled(fixedRate = INTERVALO_LEITURA_EM_MILISSEGUNDOS)
    public void gerarLeituras() {
        temperaturasBase.forEach((sensor, temperaturaBase) -> {
            double valor = gerarTemperatura(temperaturaBase);
            LeituraTemperatura leitura = new LeituraTemperatura(sensor, valor, System.currentTimeMillis());
            eventoService.publicar(leitura);
        });
    }

    private double gerarTemperatura(double temperaturaBase) {
        double variacao = ThreadLocalRandom.current().nextDouble(-2.0, 2.0);
        double leitura = temperaturaBase + variacao;

        return BigDecimal.valueOf(leitura)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
