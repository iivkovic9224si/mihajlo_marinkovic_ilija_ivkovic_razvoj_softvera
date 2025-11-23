package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.Uplata;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.services.UplataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/uplata")
public class UplataController {
    @Autowired
    UplataService uplataService;
    @Autowired
    StudentPodaciRepository studentPodaciRepository;

    @PostMapping(path = "/dodaj")
    public Long uplata(@RequestBody UplataRequest uplataRequest) {
        Uplata uplata = uplataService.dodajUplatu(uplataRequest);
        return uplata.getId();
    }

    @GetMapping(path = "/duguje/{jmbg}")
    public UplataResponse getUplata(@PathVariable String jmbg) {
        StudentPodaci studentPodaci =studentPodaciRepository.getStudentPodaciByJmbg(jmbg);

        if(studentPodaci == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student sa JMBG " + jmbg + " nije pronađen");
        }

        UplataResponse ostalo = uplataService.ostaloZaUplatu(studentPodaci);

        return ostalo;
    }

    @GetMapping("/findAll")
    public List<UplataResponse> findAll() {
        return uplataService.findAll();
    }

    @GetMapping("/findById/{id}")
    public UplataResponse findById(@PathVariable Long id) {
        return uplataService.findById(id);
    }

    @PutMapping("/update/{id}")
    public UplataResponse update(@PathVariable Long id,
                                 @RequestBody @Valid UplataRequest request) {
        return uplataService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        uplataService.delete(id);
    }

}
