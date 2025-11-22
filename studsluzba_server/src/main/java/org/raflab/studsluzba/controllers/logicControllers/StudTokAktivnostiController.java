package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.controllers.request.UpisStudentaRequest;
import org.raflab.studsluzba.controllers.response.StudijskiTokAktivnostiResponse;
import org.raflab.studsluzba.model.entity.StudijskiTokAktivnosti;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.StudijskiTokAktivnostiService;
import org.raflab.studsluzba.utils.converters.StudentIndexAktivnostConverter;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/studijski-tok")
@RequiredArgsConstructor
public class StudTokAktivnostiController {
    private final StudijskiTokAktivnostiService studijskiTokAktivnostiService;
    private final StudentIndeksRepository studentIndeksRepository;

    @PostMapping(path = "/save")
    public StudijskiTokAktivnostiResponse save(@RequestBody StudijskiTokAktivnostiRequest request) {
        StudijskiTokAktivnosti entity = StudentIndexAktivnostConverter.toStudijskiTokAktivnosti(request, studentIndeksRepository);
        StudijskiTokAktivnosti saved = studijskiTokAktivnostiService.save(entity);
        return StudentIndexAktivnostConverter.toStudijskiTokAktivnostiResponse(saved);
    }

    @GetMapping(path = "/all")
    public List<StudijskiTokAktivnostiResponse> findAll() {
        return studijskiTokAktivnostiService.findAll();
    }

    @GetMapping(path = "/{id}")
    public Optional<StudijskiTokAktivnostiResponse> findById(@PathVariable Long id) {
        return studijskiTokAktivnostiService.findById(id);
    }

    @PutMapping(path = "/update/{id}")
    public StudijskiTokAktivnostiResponse update(@PathVariable Long id, @RequestBody StudijskiTokAktivnostiRequest request) {
        StudijskiTokAktivnosti entity = StudentIndexAktivnostConverter.toStudijskiTokAktivnosti(request, studentIndeksRepository);
        entity.setId(id);
        StudijskiTokAktivnosti updated = studijskiTokAktivnostiService.update(entity);
        return StudentIndexAktivnostConverter.toStudijskiTokAktivnostiResponse(updated);
    }

    @Transactional
    @PostMapping(path = "/upisstudenta")
    public void upisiStudenta(@RequestBody UpisStudentaRequest upisStudentaRequest) {
        studijskiTokAktivnostiService.upisiStudentaNaGodinu(upisStudentaRequest.getStudijskiTokAktivnostiRequest(), upisStudentaRequest.getDrziPredmetRequests());
    }

    @Transactional
    @PostMapping(path = "/obnova")
    public void obnovaGodine(@RequestBody UpisStudentaRequest upisStudentaRequest) {
        studijskiTokAktivnostiService.obnovaGodine(
                upisStudentaRequest.getStudijskiTokAktivnostiRequest(),
                upisStudentaRequest.getDrziPredmetRequests()
        );
    }

}
