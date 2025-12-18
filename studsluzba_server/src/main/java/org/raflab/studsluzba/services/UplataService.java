package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.entity.SkolskaGodina;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.Uplata;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.UplataRepository;
import org.raflab.studsluzba.utils.converters.UplataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import request.UplataRequest;
import response.UplataResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UplataService {
    @Autowired
    private UplataRepository uplataRepository;
    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;

    private static final String KURS_API_URL =
            "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";

    public Uplata dodajUplatu(UplataRequest uplataRequest) {
        Uplata uplata = new Uplata();
        uplata.setDatumUplate(uplataRequest.getDatumUplate());
        uplata.setIznosUDinarima(uplataRequest.getIznosUDinarima());

        StudentPodaci studentPodaci = studentPodaciRepository.getStudentPodaciByJmbg(uplataRequest.getStudentJmbg());
        uplata.setStudentPodaci(studentPodaci);

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByGodina(uplataRequest.getSkolskaGodina());
        uplata.setSkolskaGodina(skolskaGodina);

        RestTemplate restTemplate = new RestTemplate();
        KursResponse kursResponse = restTemplate.getForObject(KURS_API_URL, KursResponse.class);

        if (kursResponse != null && kursResponse.getExchange_middle() != null) {
            uplata.setSrednjiKurs(kursResponse.getExchange_middle());
        }

        return uplataRepository.save(uplata);
    }

    private static class KursResponse {
        private Double exchange_middle;
        public Double getExchange_middle() { return exchange_middle; }
        public void setExchange_middle(Double exchange_middle) { this.exchange_middle = exchange_middle; }
    }

    public UplataResponse ostaloZaUplatu(StudentPodaci studentPodaci) {
        UplataResponse uplataResponse = new UplataResponse();
        Double uplacenoUEurima = uplataRepository.uplaceno(studentPodaci);

        if (uplacenoUEurima == null) {
            uplacenoUEurima = 0.0;
        }

        uplataResponse.setIznosUEurima(3000 - uplacenoUEurima);

        RestTemplate restTemplate = new RestTemplate();
        KursResponse kursResponse = restTemplate.getForObject(KURS_API_URL, KursResponse.class);

        if (kursResponse != null && kursResponse.getExchange_middle() != null) {
            uplataResponse.setIznosUDinarima(kursResponse.getExchange_middle() * uplataResponse.getIznosUEurima());
        }

        return uplataResponse;
    }

    //CRUD

    public List<UplataResponse> findAll() {
        return uplataRepository.findAll()
                .stream()
                .map(uplata -> {
                    Double uplacenoUEurima = uplataRepository.uplaceno(uplata.getStudentPodaci());
                    if (uplacenoUEurima == null) uplacenoUEurima = 0.0;
                    return UplataConverter.toUplataResponse(uplata, uplacenoUEurima);
                })
                .collect(Collectors.toList());
    }

    public UplataResponse findById(Long id) {
        Uplata uplata = uplataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uplata nije pronađena za ID: " + id));

        Double uplacenoUEurima = uplataRepository.uplaceno(uplata.getStudentPodaci());
        if (uplacenoUEurima == null) uplacenoUEurima = 0.0;

        return UplataConverter.toUplataResponse(uplata, uplacenoUEurima);
    }

    public UplataResponse update(Long id, UplataRequest request) {
        Uplata existing = uplataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uplata nije pronađena za ID: " + id));

        StudentPodaci student = studentPodaciRepository.getStudentPodaciByJmbg(request.getStudentJmbg());
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByGodina(request.getSkolskaGodina());

        RestTemplate restTemplate = new RestTemplate();
        KursResponse kursResponse = restTemplate.getForObject(UplataService.KURS_API_URL, KursResponse.class);
        Double srednjiKurs = kursResponse != null ? kursResponse.getExchange_middle() : null;

        existing.setDatumUplate(request.getDatumUplate());
        existing.setIznosUDinarima(request.getIznosUDinarima());
        existing.setStudentPodaci(student);
        existing.setSkolskaGodina(skolskaGodina);
        existing.setSrednjiKurs(srednjiKurs);

        Uplata updated = uplataRepository.save(existing);

        Double uplacenoUEurima = uplataRepository.uplaceno(updated.getStudentPodaci());
        if (uplacenoUEurima == null) uplacenoUEurima = 0.0;

        return UplataConverter.toUplataResponse(updated, uplacenoUEurima);
    }

    public void delete(Long id) {
        Uplata uplata = uplataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uplata nije pronađena za ID: " + id));
        uplataRepository.delete(uplata);
    }
}
