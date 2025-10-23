package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.services.NastavnikService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/nastavnik")
public class NastavnikController {

	@Autowired
	NastavnikService nastavnikService;
	
	@PostMapping(path = "/add")
	public Long addNewNastavnik(@RequestBody @Valid NastavnikRequest nastavnikRequest) {
		Nastavnik nastavnik = nastavnikService.save(Converters.toNastavnik(nastavnikRequest));
		return nastavnik.getId();
	}
	
	@GetMapping(path = "/all")
	public List<NastavnikResponse> getAllNastavnik() {
		return Converters.toNastavnikResponseList(nastavnikService.findAll());
	}

	@GetMapping(path = "/{id}")
	public NastavnikResponse getNastavnikById(@PathVariable Long id)
	{
		Optional<Nastavnik> rez = nastavnikService.findById(id);

		return rez.map(Converters::toNastavnikResponse).orElse(null);
	}
	
	@GetMapping(path = "/search")
	public List<NastavnikResponse> search(
			@RequestParam(required = false) String ime,
			@RequestParam(required = false) String prezime){

        return Converters.toNastavnikResponseList(nastavnikService.findByImeAndPrezime(ime, prezime));
	}
	
}
