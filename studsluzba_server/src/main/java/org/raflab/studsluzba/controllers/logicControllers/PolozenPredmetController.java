package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PolozenPredmetAwayRequest;
import org.raflab.studsluzba.controllers.request.PolozenPredmetHereRequest;
import org.raflab.studsluzba.controllers.response.PolozenPredmetResponse;
import org.raflab.studsluzba.services.PolozenPredmetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/polozenPredmet")
@RequiredArgsConstructor
public class PolozenPredmetController {

    private final PolozenPredmetService polozenPredmetService;

    @PostMapping("/save/away")
    public PolozenPredmetResponse saveAway(@RequestBody PolozenPredmetAwayRequest request) {
        return polozenPredmetService.saveAway(request);
    }

    @PostMapping("/save/here")
    public PolozenPredmetResponse saveHere(@RequestBody PolozenPredmetHereRequest request) {
        return polozenPredmetService.saveHere(request);
    }

    @GetMapping("/find/{id}")
    public PolozenPredmetResponse findById(@PathVariable Long id) {
        return polozenPredmetService.findById(id);
    }

    @GetMapping("/findAll")
    public List<PolozenPredmetResponse> findAll() {
        return polozenPredmetService.findAll();
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        polozenPredmetService.delete(id);
    }
}
