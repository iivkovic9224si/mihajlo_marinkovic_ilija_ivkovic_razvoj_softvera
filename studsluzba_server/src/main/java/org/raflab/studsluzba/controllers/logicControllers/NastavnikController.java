package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.entity.Nastavnik;
import org.raflab.studsluzba.services.NastavnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
		return nastavnikService.save(nastavnikRequest).getId();
	}
	
	@GetMapping(path = "/all")
	public List<NastavnikResponse> getAllNastavnik() {
		Iterable<NastavnikResponse> iterable = nastavnikService.findAll();
		List<NastavnikResponse> list = new ArrayList<>();
		iterable.forEach(list::add);
		return list;
	}

	@GetMapping(path = "/{id}")
	public NastavnikResponse getNastavnikById(@PathVariable Long id) {
		return nastavnikService.findById(id).orElse(null);
	}
	
	@GetMapping(path = "/search")
	public List<NastavnikResponse> search(
			@RequestParam(required = false) String ime,
			@RequestParam(required = false) String prezime){

        return nastavnikService.findByImeAndPrezime(ime, prezime);
	}
	
}
