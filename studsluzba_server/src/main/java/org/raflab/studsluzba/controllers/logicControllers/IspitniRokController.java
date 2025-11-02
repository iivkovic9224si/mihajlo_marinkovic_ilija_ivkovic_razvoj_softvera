package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.services.IspitniRokService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ispitniRok")
@RequiredArgsConstructor
public class IspitniRokController {

    private final IspitniRokService ispitniRokService;

    @PostMapping("/save")
    public IspitniRokResponse save(@RequestBody @Valid IspitniRokRequest request) {
        return ispitniRokService.save(request);
    }

    /** Vraća sve ispitne rokove iz baze, bez filtriranja po školskoj godini */
    @GetMapping("/findAll")
    public List<IspitniRokResponse> findAll() {
        return ispitniRokService.findAll(); // koristi novu findAll() iz servisa
    }

    /** Vraća sve ispitne rokove iz aktivne školske godine */
    @GetMapping("/findAllAktivna")
    public List<IspitniRokResponse> findAllAktivna() {
        return ispitniRokService.findAllAktivnaGodina();
    }

    @GetMapping("/findById/{id}")
    public IspitniRokResponse findById(@PathVariable Long id) {
        return ispitniRokService.findById(id);
    }

    @PostMapping("/update")
    public IspitniRokResponse update(@RequestBody @Valid IspitniRokRequest request) {
        return ispitniRokService.update(request);
    }

    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        ispitniRokService.delete(id);
    }
}
