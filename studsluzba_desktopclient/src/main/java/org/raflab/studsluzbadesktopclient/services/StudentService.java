package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzbadesktopclient.dtos.PageResultDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentDTO;
import org.raflab.studsluzbadesktopclient.dtos.StudentIndexDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final String baseUrl;

    private final String STUDENT_URL_PATH = "/student";

    public int update(Long id, StudentDTO student) {
        try {
            String url = createURL("updateujStudenta/" + id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<StudentDTO> entity = new HttpEntity<>(student, headers);

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
    public List<StudentDTO> sviStudenti() {
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(
                createURL("all"),
                StudentDTO[].class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return List.of(response.getBody());
        else return List.of();
    }

    // ----------------- Pretraga studenta po imenu -----------------
    public List<StudentDTO> searchStudent(String ime) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("imeandorprezime"))
                .queryParam("ime", ime);
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(
                builder.toUriString(),
                StudentDTO[].class
        );
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null)
            return List.of(response.getBody());
        else return List.of();
    }

    // ----------------- Asinkrona pretraga po srednjoj školi sa paginacijom -----------------
    public Mono<PageResultDTO<StudentDTO>> searchStudentsByHighSchoolPaged(
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
                .bodyToMono(new ParameterizedTypeReference<PageResultDTO<StudentDTO>>() {});
    }


    // ----------------- Asinkrona pretraga studenta sa paginacijom -----------------
    public Mono<PageResultDTO<StudentDTO>> searchStudentsPaged(String ime, String prezime, int page, int size) {
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
                .bodyToMono(new ParameterizedTypeReference<PageResultDTO<StudentDTO>>() {});
    }
    // ----------------- Dobavljanje svih indeksa studenta -----------------
    public List<StudentIndexDTO> getIndeksiForStudent(Long studentId) {
        ResponseEntity<StudentIndexDTO[]> response = restTemplate.getForEntity(
                baseUrl + "/student/indeksi/" + studentId,
                StudentIndexDTO[].class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return List.of(response.getBody());
        } else {
            return List.of();
        }
    }

    // ----------------- Asinkrona pretraga po broju indeksa -----------------
    // Asinhrona pretraga po broju indeksa
    public Mono<StudentIndexDTO> searchStudentByIndex(String indeksShort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(STUDENT_URL_PATH + "/fastsearch")
                        .queryParam("indeksShort", indeksShort)
                        .build()
                )
                .retrieve()
                .bodyToMono(StudentIndexDTO.class);
    }





    // ----------------- Snimanje studenta -----------------
    public Long saveStudent(StudentDTO studentDTO) {
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
