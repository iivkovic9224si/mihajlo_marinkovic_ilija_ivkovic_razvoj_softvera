package org.raflab.studsluzba.controllers.logicControllers;

import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.services.StudentPodaciService;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
import org.raflab.studsluzba.utils.converters.StudentIndexAktivnostConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(path="/api/student")
public class StudentController {

	@Autowired
	StudentIndeksRepository studentIndeksRepository;

	@Autowired
	StudentPodaciService studentPodaciService;

	@Autowired
	StudentIndeksService studentIndeksService;

	@Autowired
	StudijskiProgramRepository studijskiProgramRepository;

    @Autowired
    EntityMappers entityMappers;



    @PostMapping(path="/add")
   	public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaciRequest) {
   	    return studentPodaciService.save(studentPodaciRequest).getId();
 	}

	//Ispisi sve redove iz tabele Student Podaci
	@GetMapping(path="/all")
	public Iterable<StudentPodaciResponse> getAllStudentPodaci() {
        return studentPodaciService.findAll();
	}

	//Isto kao i ovo gore (ispisi sve redove iz tabele Student Podaci) samo Paginated
	@GetMapping(path="/svi")
	public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		return studentPodaciService.findAll(PageRequest.of(page, size));
	}

	//Nadji Student Podaci preko id-ja studenta
	@GetMapping(path="/podaci/{id}")
	public Optional<StudentPodaciResponse> getStudentPodaci(@PathVariable Long id){
		return studentPodaciService.findById(id);
	}

	//Dodaj novi red u tabelu Student Indeks
	@PostMapping(path="/saveindeks")
	public Long saveIndeks(@RequestBody StudentIndeksRequest studentIndeksRequest) {
		return studentIndeksService.saveIndeks(studentIndeksRequest);
	}


	//Ispisi red iz tabele Student Indeks po id-ju studenta (student Indeks)
    @GetMapping(path="/indeks/{id}")
    public StudentIndeksResponse getStudentIndeks(@PathVariable Long id){
    	Optional<StudentIndeks> rez = studentIndeksRepository.findById(id);
    	if(rez.isEmpty()) return null;
    	else {
    		StudentIndeks retVal = rez.get();
    		return entityMappers.fromStudentIndexToResponse(retVal);
    	}

    }

	//Ispisi Student Indeks redove prema id-u iz tabele Student Podaci
    @GetMapping(path="/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci){
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci)
                .stream()
                .map(entityMappers::fromStudentIndexToResponse) // map each entity to response
                .collect(Collectors.toList());
    }

    @GetMapping(path="/fastsearch")
    public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
        String[] parsedData = ParseUtils.parseIndeks(indeksShort);
        if (parsedData != null) {
            String oznaka = parsedData[0];
            int godina = 2000 + Integer.parseInt(parsedData[1]);
            int broj = Integer.parseInt(parsedData[2]);

            StudentIndeks si = studentIndeksRepository.findStudentIndeksWithStudent(oznaka, godina, broj);
            return entityMappers.fromStudentIndexToResponse(si); // koristiš postojeći mapper
        }
        return null;
    }


    //Vraca Student Indeks preko stringa oblika iivkovic9224si@raf.rs
    @GetMapping(path="/emailsearch")  // salje se email studenta
    public StudentIndeksResponse emailSearch(@RequestParam String studEmail) {
      String[] parsedData = ParseUtils.parseEmail(studEmail);
      if(parsedData!=null) {
    	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));
    	  return entityMappers.fromStudentIndexToResponse(si);
      }else return null;
    }

    @GetMapping(path="/profile/{studentIndeksId}")
    public StudentProfileDTO getStudentProfile(@PathVariable  Long studentIndeksId) {
    	return studentPodaciService.getStudentProfile(studentIndeksId);
    }

    @GetMapping(path="/webprofile/{studentIndeksId}")
    public StudentWebProfileDTO getStudentWebProfile(@PathVariable  Long studentIndeksId) {
    	return studentPodaciService.getStudentWebProfile(studentIndeksId);
    }

    @GetMapping(path="/webprofile/email")
    public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
    	 String[] parsedData = ParseUtils.parseEmail(studEmail);
         if(parsedData!=null) {
       	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));
       	  if(si!=null)
       		  return studentPodaciService.getStudentWebProfile(si.getId());
         }
         return null;
    }

	//selekcija svih položenih ispita za broj indeksa studenta
	@PostMapping(path="/polozenipredmeti")
	public Page<PredmetResponse> polozeniPredmeti(
			@RequestBody @Valid StudentIndeksRequest studentIndeksRequest,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return studentIndeksService.findPolozeniPredmetiByStudentIndeks(studentIndeksRequest, page, size);
	}

	//selekcija svih nepoloženih ispita za broj indeksa studenta, paginirano
	@PostMapping(path="/nepolozenipredmeti")
	public Page<PredmetResponse> nepolozeniPredmeti(
			@RequestBody @Valid StudentIndeksRequest studentIndeksRequest,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return studentIndeksService.findNePolozeniPredmetiByStudentIndeks(studentIndeksRequest, page, size);
	}

	//pregled svih upisanih godina za broj indeksa
	@PostMapping(path = "/upisanegodine")
	public List<Integer> upisaneGodine(@RequestBody @Valid StudentIndeksRequest studentIndeksRequest) {
		return studentIndeksService.getUpisaneGodineByStudentIndeks(studentIndeksRequest);
	}

	//pregled obnovljenih godina za broj indeksa
	@PostMapping(path = "/obnovljenegodine")
	public List<Integer> obnovljeneGodine(@RequestBody @Valid StudentIndeksRequest studentIndeksRequest) {
		return studentIndeksService.getObnovljeneGodine(studentIndeksRequest);
	}

	//selekcija studenata na osnovu imena i/ili prezimena (može samo ime, ili samo prezime ili oba da se unesu)
	@GetMapping(path = "/imeandorprezime")
	public Page<StudentPodaciResponse> getStudentByImeandorPrezime(@RequestParam(required = false) String ime, @RequestParam(required = false) String prezime, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("ime").ascending());
		return studentIndeksService.getStudentPodaciByImeandorPrezime(ime, prezime, pageable);
	}


	//Update postojeceg studenta
	@PutMapping(path = "/updateujStudenta/{id}")
	public Long updateStudent(@PathVariable Long id, @RequestBody @Valid StudentPodaciRequest studentPodaciRequest) {
		return studentPodaciService.update(id, studentPodaciRequest);
	}

	// Update postojeceg reda u tabeli Student Indeks
	@PutMapping(path="/updateindeks/{id}")
	public Long updateIndeks(@PathVariable Long id, @RequestBody StudentIndeksRequest request) {
		return studentIndeksService.update(id, request);
	}

	//Selekcija studenta (njegovih ličnih podataka) preko broja indeksa
	@GetMapping(path = "/nadjiStudentaPrekoStudentIndeksa")
	public StudentPodaciResponse nadjiStudentaPrekoStudentIndeks(@RequestParam String studProgramOznaka, @RequestParam int broj, @RequestParam int godina){
		return studentIndeksService.getStudentPodaciByStudentIndeks(studProgramOznaka, godina, broj);
	}

    @GetMapping(path = "/srednjaskola")
    public Page<StudentPodaciResponse> nadjiStudenteBySrednjaSkola(
            @RequestParam String srednjaSkolaSifra,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return studentIndeksService.getStudentPodaciBySrednjaSkola(
                srednjaSkolaSifra,
                page,
                size
        );
    }

	@PostMapping(path = "/obrisiStudentIndeks")
	public void obrisiStudentIndeks(@RequestBody StudentIndeksRequest request) {
		studentIndeksService.deleteByStudentIndeks(
				request.getStudProgramOznaka(),
				request.getBroj(),
				request.getGodina()
		);
	}

	@PostMapping(path = "/obrisiStudentPodaci")
	public void obrisiStudentPodaci(@RequestBody StudentPodaciRequest request) {
		studentPodaciService.deleteStudentPodaci(request);
	}

    // Vraća sve indekse za dati StudentPodaci ID
    @GetMapping(path="/indeksi/all/{studentPodaciId}")
    public List<StudentIndeksResponse> getAllIndeksiForStudentPodaci(@PathVariable Long studentPodaciId) {
        return studentIndeksService.getAllIndeksiForStudentPodaci(studentPodaciId);
    }

}
