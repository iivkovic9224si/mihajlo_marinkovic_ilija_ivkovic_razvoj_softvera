package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.OstvareniPredispitniRequest;
import org.raflab.studsluzba.controllers.response.OstvareniPredispitniResponse;
import org.raflab.studsluzba.services.OstvareniPredispitniService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ostvareniPredispitni")
@RequiredArgsConstructor
public class OstvareniPredispitniController {

    private final OstvareniPredispitniService ostvareniPredispitniService;

    @PostMapping("/save")
    public OstvareniPredispitniResponse create(@RequestBody OstvareniPredispitniRequest request) {
        return ostvareniPredispitniService.save(request);
    }

    @GetMapping("/findAll")
    public List<OstvareniPredispitniResponse> getAll() {
        return ostvareniPredispitniService.findAll();
    }

    @GetMapping("/findById/{id}")
    public OstvareniPredispitniResponse getById(@PathVariable Long id) {
        return ostvareniPredispitniService.findById(id);
    }

    @PostMapping("/update")
    public OstvareniPredispitniResponse update(@RequestBody OstvareniPredispitniRequest request) {
        return ostvareniPredispitniService.update(request);
    }


    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        ostvareniPredispitniService.delete(id);
    }

    @GetMapping("/poeni")
    public List<OstvareniPredispitniResponse> getOstvareniPoeni(
            @RequestParam Long studentIndeksId,
            @RequestParam Long predmetId,
            @RequestParam String skolskaGodina) {
        return ostvareniPredispitniService.getOstvareniPoeniZaStudentaNaPredmetu(
                studentIndeksId, predmetId, skolskaGodina);
    }
}
