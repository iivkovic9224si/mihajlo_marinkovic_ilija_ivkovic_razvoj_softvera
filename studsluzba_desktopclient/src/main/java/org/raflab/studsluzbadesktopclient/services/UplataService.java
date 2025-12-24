package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import request.UplataRequest;
import response.UplataResponse;

@Service
public class UplataService {

    private final WebClient webClient;

    public UplataService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api/uplata").build();
    }

    public Mono<Long> dodajUplatu(UplataRequest req) {
        return webClient.post()
                .uri("/dodaj")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Long.class)
                .doOnError(err -> System.err.println("dodajUplatu error: " + err.getMessage()));
    }

    public Mono<UplataResponse> dugujeZaStudenta(String jmbg) {
        return webClient.get()
                .uri("/duguje/{jmbg}", jmbg)
                .retrieve()
                .bodyToMono(UplataResponse.class)
                .doOnError(err -> System.err.println("dug error: " + err.getMessage()));
    }

    // ✅ NOVO: Istorija uplata
    public Flux<UplataResponse> getUplateZaStudenta(String jmbg) {
        return webClient.get()
                .uri("/student/{jmbg}", jmbg)
                .retrieve()
                .bodyToFlux(UplataResponse.class)
                .doOnError(err -> System.err.println("uplate error: " + err.getMessage()));
    }
}
