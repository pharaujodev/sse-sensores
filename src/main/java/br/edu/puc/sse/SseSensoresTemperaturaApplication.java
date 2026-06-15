package br.edu.puc.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SseSensoresTemperaturaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SseSensoresTemperaturaApplication.class, args);
    }
}
