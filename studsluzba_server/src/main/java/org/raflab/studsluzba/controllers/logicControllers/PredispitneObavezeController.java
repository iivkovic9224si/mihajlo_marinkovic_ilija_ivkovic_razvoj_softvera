package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredispitneObavezeRequest;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.services.PredispitneObavezeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/predispitneObaveze")
@RequiredArgsConstructor
public class PredispitneObavezeController {

    private final PredispitneObavezeService predispitneObavezeService;

    // CREATE
    @PostMapping("/save")
    public PredispitneObavezeResponse save(@RequestBody @Valid PredispitneObavezeRequest request) {
        return predispitneObavezeService.save(request);
    }

    // UPDATE
    @PostMapping("/update")
    public PredispitneObavezeResponse update(@RequestBody @Valid PredispitneObavezeRequest request) {
        return predispitneObavezeService.update(request);
    }

    // FIND ALL
    @GetMapping("/findAll")
    public List<PredispitneObavezeResponse> findAll() {
        return predispitneObavezeService.findAll();
    }

    // FIND BY ID
    @GetMapping("/findById/{id}")
    public PredispitneObavezeResponse findById(@PathVariable Long id) {
        return predispitneObavezeService.findById(id);
    }

    // FIND BY PREDMET (preko sifre predmeta)
    @GetMapping("/findByPredmet/{sifraPredmeta}")
    public List<PredispitneObavezeResponse> findByPredmet(@PathVariable String sifraPredmeta) {
        return predispitneObavezeService.findByPredmet(sifraPredmeta);
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        predispitneObavezeService.delete(id);
    }
}
