package org.raflab.studsluzba.controllers.logicControllers;

import lombok.RequiredArgsConstructor;

import org.raflab.studsluzba.services.NastavnikService;
import org.springframework.web.bind.annotation.*;
import request.NastavnikRequest;
import response.DrziPredmetResponse;
import response.NastavnikResponse;
import response.NastavnikZvanjeResponse;
import response.VisokoSkolskaUstanovaResponse;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/nastavnik")
@RequiredArgsConstructor
public class NastavnikController {

    private final NastavnikService nastavnikService;

    @PostMapping("/save")
    public NastavnikResponse save(@RequestBody @Valid NastavnikRequest request) {
        return nastavnikService.save(request);
    }

    @GetMapping("/findAll")
    public List<NastavnikResponse> findAll() {
        return nastavnikService.findAll();
    }

    @GetMapping("/findById/{id}")
    public NastavnikResponse findById(@PathVariable Long id) {
        return nastavnikService.findById(id);
    }

    @GetMapping("/findByEmail/{email}")
    public NastavnikResponse findByEmail(@PathVariable String email) {
        return nastavnikService.findByEmail(email);
    }

    @PostMapping("/update")
    public NastavnikResponse update(@RequestBody @Valid NastavnikRequest request) {
        return nastavnikService.update(request);
    }

    @GetMapping("/delete")
    public void delete(@RequestParam(required = false) Long id,
                       @RequestParam(required = false) String email) {
        nastavnikService.delete(id, email);
    }

    // DODATNI UPITI
    @GetMapping("/zvanja/{email}")
    public List<NastavnikZvanjeResponse> getZvanja(@PathVariable String email) {
        return nastavnikService.getZvanja(email);
    }

    @GetMapping("/aktivnoZvanje/{email}")
    public NastavnikZvanjeResponse getAktivnoZvanje(@PathVariable String email) {
        return nastavnikService.getAktivnoZvanje(email);
    }

    @GetMapping("/ustanove/{email}")
    public List<VisokoSkolskaUstanovaResponse> getUstanove(@PathVariable String email) {
        return nastavnikService.getUstanove(email);
    }

    @GetMapping("/drziPredmete/{email}")
    public List<DrziPredmetResponse> getDrziPredmete(@PathVariable String email) {
        return nastavnikService.getDrziPredmete(email);
    }
}
