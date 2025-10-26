package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.VisokoSkolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoSkolskaUstanovaResponse;
import org.raflab.studsluzba.model.entity.VrstaVisokoSkolskihUstanova;
import org.raflab.studsluzba.services.VisokoSkolskaUstanovaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/visokoSkolskaUstanova")
public class VisokoSkolskaUstanovaController {

    @Autowired
    private VisokoSkolskaUstanovaService visokoSkolskaUstanovaService;

    //-------------------------CREATE-------------------------
    @PostMapping("/save")
    public VisokoSkolskaUstanovaResponse save(@RequestBody VisokoSkolskaUstanovaRequest request) {
        return visokoSkolskaUstanovaService.save(request);
    }

    //-------------------------READ-------------------------
    @GetMapping("/all")
    public List<VisokoSkolskaUstanovaResponse> findAll() {
        return visokoSkolskaUstanovaService.findAll();
    }

    @GetMapping("/findById/{id}")
    public Optional<VisokoSkolskaUstanovaResponse> findByID(@PathVariable Long id) {
        return Optional.ofNullable(visokoSkolskaUstanovaService.findByID(id));
    }

    //-------------------------UPDATE PO STAROM NAZIVU I VRSTI-------------------------
    @PostMapping("/update")
    public VisokoSkolskaUstanovaResponse update(
            @RequestParam String stariNaziv,
            @RequestParam VrstaVisokoSkolskihUstanova staraVrsta,
            @RequestBody VisokoSkolskaUstanovaRequest noviPodaci) {

        return visokoSkolskaUstanovaService.update(stariNaziv, staraVrsta, noviPodaci);
    }

    //-------------------------DELETE-------------------------
    @GetMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        visokoSkolskaUstanovaService.delete(id);
    }

}

