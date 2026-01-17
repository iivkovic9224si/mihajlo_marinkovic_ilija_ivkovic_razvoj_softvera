package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import request.PredmetRequest;
import response.PredmetResponse;

@Service
public class PredmetClientService {

    private final WebClient webClient;

    public PredmetClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api").build();
    }

    public Mono<Void> save(PredmetRequest req) {
        System.out.println("PredmetClientService.save called: " + req.getNaziv());
        return webClient.post()
                .uri("/predmet/save")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(err -> System.out.println("Error in save WebClient: " + err))
                .doOnSuccess(v -> System.out.println("WebClient save success"));
    }

    public Mono<PredmetResponse> findBySifra(String sifra) {
        return webClient.get()
                .uri("/findBySifra/{sifra}", sifra)
                .retrieve()
                .bodyToMono(PredmetResponse.class);
    }

    public Mono<Double> getProsecnaOcena(String sifra, int od, int doG) {
        System.out.println("PredmetClientService.getProsecnaOcena called for: " + sifra);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/predmet/prosecnaOcena/{sifra}")
                        .queryParam("godinaOd", od)
                        .queryParam("godinaDo", doG)
                        .build(sifra))
                .retrieve()
                .bodyToMono(Double.class)
                .doOnError(err -> System.out.println("Error in getProsecnaOcena WebClient: " + err))
                .doOnSuccess(prosek -> System.out.println("WebClient getProsecnaOcena success: " + prosek));
    }
}
