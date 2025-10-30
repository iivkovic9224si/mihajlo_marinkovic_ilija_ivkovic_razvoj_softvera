package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiTokAktivnostiRequest;
import org.raflab.studsluzba.controllers.response.StudijskiTokAktivnostiResponse;
import org.raflab.studsluzba.model.entity.StudijskiTokAktivnosti;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.StudijskiTokAktivnostiService;
import org.raflab.studsluzba.utils.converters.StudentIndexAktivnostController;
import org.springframework.web.bind.annotation.*;

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
        StudijskiTokAktivnosti entity = StudentIndexAktivnostController.toStudijskiTokAktivnosti(request, studentIndeksRepository);
        StudijskiTokAktivnosti saved = studijskiTokAktivnostiService.save(entity);
        return StudentIndexAktivnostController.toStudijskiTokAktivnostiResponse(saved);
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
        StudijskiTokAktivnosti entity = StudentIndexAktivnostController.toStudijskiTokAktivnosti(request, studentIndeksRepository);
        entity.setId(id);
        StudijskiTokAktivnosti updated = studijskiTokAktivnostiService.update(entity);
        return StudentIndexAktivnostController.toStudijskiTokAktivnostiResponse(updated);
    }
}
