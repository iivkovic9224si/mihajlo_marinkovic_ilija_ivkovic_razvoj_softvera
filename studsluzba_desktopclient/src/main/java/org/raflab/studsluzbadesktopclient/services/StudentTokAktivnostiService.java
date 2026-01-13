package org.raflab.studsluzbadesktopclient.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import pomocniDTO.StudentUpisaneGodineRequest;
import request.DrziPredmetRequest;
import request.StudijskiTokAktivnostiRequest;
import response.DrziPredmetResponse;
import response.StudijskiTokAktivnostiResponse;

import java.util.Comparator;
import java.util.List;

@Service
public class StudentTokAktivnostiService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/studijski-tok/";

    public StudentTokAktivnostiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int upisiNaGodinu(StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        return send("upisstudenta", req, predmeti);
    }

    public int obnovaGodine(StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        return send("obnova", req, predmeti);
    }

    private int send(String path, StudijskiTokAktivnostiRequest req, List<DrziPredmetRequest> predmeti) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        try {
            ResponseEntity<List<DrziPredmetResponse>> drziResp = restTemplate.exchange(
                    "http://localhost:8090/api/drziPredmet/findAll",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<DrziPredmetResponse>>() {});

            List<DrziPredmetRequest> validPredmeti = drziResp.getBody().stream()
                    .filter(dpr -> predmeti.stream().anyMatch(p ->
                            p.getPredmetSifra().equals(dpr.getPredmetSifra())
                                    && p.getNastavnikEmail().equals(dpr.getNastavnikEmail())   // ✅ bitno da se poklopi i nastavnik
                    ))
                    .map(this::mapToRequest)
                    .toList();

            String url = baseUrl + path;
            AktivnostPayload payload = new AktivnostPayload(req, validPredmeti);

            restTemplate.postForEntity(url, new HttpEntity<>(payload, headers), Void.class);
            return 1;

        } catch (RestClientResponseException e) {
            // ✅ ovde hvataš i 400 i 500 i vadiš poruku iz tela
            String body = e.getResponseBodyAsString(); // JSON error body [web:145][web:144]
            throw new RuntimeException(extractMessage(body), e);
        }
    }

    private String extractMessage(String body) {
        try {
            JsonNode node = new ObjectMapper().readTree(body);
            if (node.hasNonNull("message")) return node.get("message").asText();
            return body;
        } catch (Exception ex) {
            return body;
        }
    }


    private DrziPredmetRequest mapToRequest(DrziPredmetResponse dpr) {
        DrziPredmetRequest req = new DrziPredmetRequest();
        req.setPredmetSifra(dpr.getPredmetSifra());
        req.setNastavnikEmail(dpr.getNastavnikEmail());
        return req;
    }

    // omot klasa za JSON telo
    public static class AktivnostPayload {
        private StudijskiTokAktivnostiRequest studijskiTokAktivnostiRequest;
        private List<DrziPredmetRequest> drziPredmetRequests;

        public AktivnostPayload() {}
        public AktivnostPayload(StudijskiTokAktivnostiRequest r, List<DrziPredmetRequest> l) {
            this.studijskiTokAktivnostiRequest = r;
            this.drziPredmetRequests = l;
        }

        // getteri/setteri
        public StudijskiTokAktivnostiRequest getStudijskiTokAktivnostiRequest() {
            return studijskiTokAktivnostiRequest;
        }
        public void setStudijskiTokAktivnostiRequest(StudijskiTokAktivnostiRequest s) {
            this.studijskiTokAktivnostiRequest = s;
        }
        public List<DrziPredmetRequest> getDrziPredmetRequests() {
            return drziPredmetRequests;
        }
        public void setDrziPredmetRequests(List<DrziPredmetRequest> d) {
            this.drziPredmetRequests = d;
        }
    }

    public Integer getPoslednjaGodina(int godinaIndeksa, int brojIndeksa, String studProgramOznaka) {
        try {
            StudentUpisaneGodineRequest req = new StudentUpisaneGodineRequest();
            req.setGodina(godinaIndeksa);
            req.setBroj(brojIndeksa);
            req.setStudProgramOznaka(studProgramOznaka);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);  // ✅ FIX

            HttpEntity<StudentUpisaneGodineRequest> entity = new HttpEntity<>(req, headers);

            ResponseEntity<List<Integer>> resp = restTemplate.exchange(
                    "http://localhost:8090/api/student/upisanegodine",
                    HttpMethod.POST, entity,  // ✅ proslijedi headers
                    new ParameterizedTypeReference<List<Integer>>(){});

            List<Integer> godine = resp.getBody();
            return godine.isEmpty() ? null : godine.get(godine.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
