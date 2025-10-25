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
import org.raflab.studsluzba.repositories.PolozenPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
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
	StudentPodaciRepository studentPodaciRepository;
	
	@Autowired
	StudentIndeksRepository studentIndeksRepository;
	
	@Autowired
	StudentProfileService studentProfileService;

	@Autowired
	StudentIndeksService studentIndeksService;

	@Autowired
	StudijskiProgramRepository studijskiProgramRepository;

    @Autowired
    EntityMappers entityMappers;

    @PostMapping(path="/add")
   	public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaci) {
    	
   	    StudentPodaci sp = studentPodaciRepository.save(Converters.toStudentPodaci(studentPodaci));
   	    
   	    return sp.getId();
 	}


	//Ispisi sve redove iz tabele Student Podaci
	@GetMapping(path="/all")
	public Iterable<StudentPodaciResponse> getAllStudentPodaci() {
        return studentPodaciRepository.findAll().stream()
                .map(entityMappers::fromStudentPodaciToResponse)
                .collect(Collectors.toList());
	}

	//Isto kao i ovo gore (ispisi sve redove iz tabele Student Podaci) samo Paginated
	@GetMapping(path="/svi")
	public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		return studentPodaciRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(entityMappers::fromStudentPodaciToResponse);
	}

	//Nadji Student Podaci preko id-ja studenta
	@GetMapping(path="/podaci/{id}")
	public StudentPodaciResponse getStudentPodaci(@PathVariable Long id){
		Optional<StudentPodaci> rez = studentPodaciRepository.findById(id);
		if(rez.isEmpty()) return null;
		return entityMappers.fromStudentPodaciToResponse(rez.get());
	}

	//Dodaj novi red u tabelu Student Indeks
    @PostMapping(path="/saveindeks")
   	public Long saveIndeks(@RequestBody StudentIndeksRequest studentIndeksRequest) {
		StudentIndeks studentIndeks = Converters.toStudentIndeks(studentIndeksRequest);

		int nextBroj = studentIndeksService.findBroj(studentIndeksRequest.getGodina(), studentIndeksRequest.getStudProgramOznaka());
		studentIndeks.setBroj(nextBroj);

		Optional<StudentPodaci> studentPodaci = studentPodaciRepository.findById(studentIndeksRequest.getStudentId());
		if(studentPodaci.isEmpty()) return null;		//TODO - throw error if not exist
		studentIndeks.setStudent(studentPodaci.get());

		List<StudijskiProgram> studijskiProgrami = studijskiProgramRepository.findByOznaka(studentIndeksRequest.getStudProgramOznaka());
		if (studijskiProgrami.isEmpty()) {
            throw new RuntimeException("No StudijskiProgram found with oznaka: " + studentIndeksRequest.getStudProgramOznaka());
		}
		studentIndeks.setStudijskiProgram(studijskiProgrami.get(0));

		try {
			StudentIndeks savedStudentIndeks = studentIndeksRepository.save(studentIndeks);
			return savedStudentIndeks.getId();
		} catch (DataIntegrityViolationException e) {
			// TODO
			throw new RuntimeException("Duplicate entry for broj, godina, and studProgramOznaka", e);
		} catch (Exception e) {
			throw new RuntimeException("Error while saving the StudentIndeks.", e);
		}
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

	//Ispisi Student Indeks prema id-u iz tabele Student Podaci
    @GetMapping(path="/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci){
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci)
                .stream()
                .map(entityMappers::fromStudentIndexToResponse) // map each entity to response
                .collect(Collectors.toList());
    }

	//Vraca Student Indeks preko stringa oblika si9224
    @GetMapping(path="/fastsearch")  // salje se string oblika rn1923 - smer godina broj
    public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
      String[] parsedData = ParseUtils.parseIndeks(indeksShort);
      if(parsedData!=null) {
    	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));	
    	  return entityMappers.fromStudentIndexToResponse(si);
      }else return null;
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
    
    @GetMapping(path="/search")  // pretraga po imenu, prezimenu i elementima indeksa
    public Page<StudentDTO> search(@RequestParam (required = false) String ime,
								   @RequestParam (required = false) String prezime,
								   @RequestParam (required = false) String studProgram,
								   @RequestParam (required = false) Integer godina,
								   @RequestParam (required = false) Integer broj,
								   @RequestParam(defaultValue = "0") Integer page,
								   @RequestParam(defaultValue = "10") Integer size) {

		if(studProgram==null && godina == null && broj==null) { // pretrazivanje studenata bez indeksa
    		Page<StudentPodaci> spList = studentPodaciRepository.findStudent(ime, prezime, PageRequest.of(page, size, Sort.by("id").descending()));
			return spList.map(EntityMappers::fromStudentPodaciToDTO);
    	}
    	Page<StudentIndeks> siList = studentIndeksRepository.findStudentIndeks(ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()));
		return siList.map(EntityMappers::fromStudentIndeksToDTO);
    }

    @GetMapping(path="/profile/{studentIndeksId}")  
    public StudentProfileDTO getStudentProfile(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentProfile(studentIndeksId);   	
    }
    
    @GetMapping(path="/webprofile/{studentIndeksId}")  
    public StudentWebProfileDTO getStudentWebProfile(@PathVariable  Long studentIndeksId) {
    	return studentProfileService.getStudentWebProfile(studentIndeksId);   	
    }
    
    @GetMapping(path="/webprofile/email")  
    public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
    	 String[] parsedData = ParseUtils.parseEmail(studEmail);
         if(parsedData!=null) {
       	  StudentIndeks si = studentIndeksRepository.findStudentIndeks(parsedData[0], 2000+Integer.parseInt(parsedData[1]),Integer.parseInt(parsedData[2]));
       	  if(si!=null)
       		  return studentProfileService.getStudentWebProfile(si.getId());   	
         }
         return null;
    }

	//selekcija svih položenih ispita za broj indeksa studenta
	@PostMapping(path="/polozenipredmeti")
	public List<PredmetResponse> polozeniPredmeti(@RequestBody @Valid StudentIndeksRequest studentIndeksRequest) {
		StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
				studentIndeksRequest.getStudProgramOznaka(),
				studentIndeksRequest.getGodina(),
				studentIndeksRequest.getBroj()
		);

		if (studentIndeks == null) {
			throw new RuntimeException("Student indeks nije pronađen u bazi.");
		}

		return studentIndeksService.findPolozeniPredmetiByStudentIndeks(studentIndeks);
	}

	//pregled svih upisanih godina za broj indeksa
	@PostMapping(path = "/upisanegodine")
	public List<Integer> upisaneGodine(@RequestBody @Valid StudentIndeksRequest studentIndeksRequest) {
		StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
				studentIndeksRequest.getStudProgramOznaka(),
				studentIndeksRequest.getGodina(),
				studentIndeksRequest.getBroj()
		);

		if (studentIndeks == null) {
			throw new RuntimeException("Student indeks nije pronađen u bazi.");
		}

		return studentIndeksService.getUpisaneGodineByStudentIndeks(studentIndeks);
	}

	//pregled obnovljenih godina za broj indeksa
	@PostMapping(path = "/obnovljenegodine")
	public List<Integer> obnovljeneGodine(@RequestBody @Valid StudentIndeksRequest studentIndeksRequest) {
		StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks(
				studentIndeksRequest.getStudProgramOznaka(),
				studentIndeksRequest.getGodina(),
				studentIndeksRequest.getBroj()
		);

		if (studentIndeks == null) {
			throw new RuntimeException("Student indeks nije pronađen u bazi.");
		}

		return studentIndeksService.getObnovljeneGodineByStudentIndeks(studentIndeks);
	}

	@GetMapping(path = "/imeandorprezime")
	public Page<StudentPodaciResponse> getStudentByImeandorPrezime(@RequestParam(required = false) String ime, @RequestParam(required = false) String prezime, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("ime").ascending());
		return studentIndeksService.getStudentPodaciByImeandorPrezime(ime, prezime, pageable);
	}
}
