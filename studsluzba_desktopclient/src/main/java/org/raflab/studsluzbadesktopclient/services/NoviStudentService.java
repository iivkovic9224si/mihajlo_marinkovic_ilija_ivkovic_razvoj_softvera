package org.raflab.studsluzbadesktopclient.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import request.StudentIndeksRequest;
import request.StudentPodaciRequest;

@Service
public class NoviStudentService {

    private final WebClient webClient;

    public NoviStudentService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8090/api/student").build();
    }

    // fallback ako Spring ne ubrizga builder
    public NoviStudentService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8090/api/student")
                .build();
    }

    public Mono<Long> dodajStudenta(StudentPodaciRequest req) {
        return webClient.post()
                .uri("/add")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<Long> sacuvajIndeks(StudentIndeksRequest req) {
        return webClient.post()
                .uri("/saveindeks")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Long.class);
    }
}
