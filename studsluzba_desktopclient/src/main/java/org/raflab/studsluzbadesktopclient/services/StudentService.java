package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import pomocniDTO.PageResultDTO;
import reactor.core.publisher.Mono;
import response.StudentIndeksResponse;
import response.StudentPodaciResponse;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final String baseUrl;

    private final String STUDENT_URL_PATH = "/student";

    public int update(Long id, StudentPodaciResponse student) {
        try {
            String url = createURL("updateujStudenta/" + id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<StudentPodaciResponse> entity = new HttpEntity<>(student, headers);

            // možeš koristiti exchange za PUT
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private String createURL(String pathEnd) {
        return baseUrl + STUDENT_URL_PATH + "/" + pathEnd;
    }

    // ----------------- Dobavljanje svih studenata -----------------
    public List<StudentPodaciResponse> sviStudenti() {
        ResponseEntity<StudentPodaciResponse[]> response = restTemplate.getForEntity(
                createURL("all"),
                StudentPodaciResponse[].class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return List.of(response.getBody());
        else return List.of();
    }

    // ----------------- Pretraga studenta po imenu -----------------
    public List<StudentPodaciResponse> searchStudent(String ime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("imeandorprezime"))
                .queryParam("ime", ime);
        ResponseEntity<StudentPodaciResponse[]> response = restTemplate.getForEntity(
                builder.toUriString(),
                StudentPodaciResponse[].class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return List.of(response.getBody());
        else return List.of();
    }

    // ----------------- Asinkrona pretraga po srednjoj školi sa paginacijom -----------------
    public Mono<PageResultDTO<StudentPodaciResponse>> searchStudentsByHighSchoolPaged(
            String srednjaSkolaSifra,
            int page,
            int size
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(STUDENT_URL_PATH + "/srednjaskola")
                        .queryParam("srednjaSkolaSifra", srednjaSkolaSifra)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }


    // ----------------- Asinkrona pretraga studenta sa paginacijom -----------------
    public Mono<PageResultDTO<StudentPodaciResponse>> searchStudentsPaged(String ime, String prezime, int page, int size) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(STUDENT_URL_PATH + "/imeandorprezime")
                            .queryParam("page", page)
                            .queryParam("size", size);

                    if (ime != null && !ime.isEmpty()) {
                        uriBuilder.queryParam("ime", ime + "%"); // dodaj % za like pretragu
                    }
                    if (prezime != null && !prezime.isEmpty()) {
                        uriBuilder.queryParam("prezime", prezime + "%");
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PageResultDTO<StudentPodaciResponse>>() {});
    }
    // ----------------- Dobavljanje svih indeksa studenta -----------------
    public List<StudentIndeksResponse> getIndeksiForStudent(Long studentId) {
        ResponseEntity<StudentIndeksResponse[]> response = restTemplate.getForEntity(
                baseUrl + "/student/indeksi/" + studentId,
                StudentIndeksResponse[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return List.of(response.getBody());
        } else {
            return List.of();
        }
    }

    // ----------------- Asinkrona pretraga po broju indeksa -----------------
    // Asinhrona pretraga po broju indeksa
    public Mono<StudentIndeksResponse> searchStudentByIndex(String indeksShort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(STUDENT_URL_PATH + "/fastsearch")
                        .queryParam("indeksShort", indeksShort)
                        .build()
                )
                .retrieve()
                .bodyToMono(StudentIndeksResponse.class);
    }





    // ----------------- Snimanje studenta -----------------
    public Long saveStudent(StudentPodaciResponse studentDTO) {
        ResponseEntity<Long> response = restTemplate.postForEntity(
                createURL("add"),
                new HttpEntity<>(studentDTO),
                Long.class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return response.getBody();
        else return null;
    }
}
