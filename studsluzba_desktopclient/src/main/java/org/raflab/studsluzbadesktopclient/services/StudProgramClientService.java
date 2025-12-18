package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import request.StudijskiProgramRequest;
import response.PredmetResponse;
import response.StudijskiProgramResponse;

@Service
public class StudProgramClientService {

    private final WebClient webClient;

    public StudProgramClientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api").build();
    }

    public Flux<StudijskiProgramResponse> getAll() {
        System.out.println("StudProgramClientService.getAll called");
        return webClient.get()
                .uri("/studprogram/all")
                .retrieve()
                .bodyToFlux(StudijskiProgramResponse.class)
                .doOnError(err -> System.out.println("Error in getAll WebClient: " + err))
                .doOnNext(sp -> System.out.println("Received program: " + sp.getNaziv()));
    }

    public Flux<PredmetResponse> getPredmeti(StudijskiProgramRequest req) {
        System.out.println("StudProgramClientService.getPredmeti called for program: " + req.getOznaka());
        return webClient.post()
                .uri("/studprogram/predmeti")
                .bodyValue(req)
                .retrieve()
                .bodyToFlux(PredmetResponse.class)
                .doOnError(err -> System.out.println("Error in getPredmeti WebClient: " + err))
                .doOnNext(p -> System.out.println("Received predmet: " + p.getNaziv()));
    }
}
