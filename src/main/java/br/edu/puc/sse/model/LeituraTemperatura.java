package br.edu.puc.sse.model;

public class LeituraTemperatura {

    private final String sensor;
    private final double valor;
    private final long timestamp;

    public LeituraTemperatura(String sensor, double valor, long timestamp) {
        this.sensor = sensor;
        this.valor = valor;
        this.timestamp = timestamp;
    }

    public String getSensor() {
        return sensor;
    }

    public double getValor() {
        return valor;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
