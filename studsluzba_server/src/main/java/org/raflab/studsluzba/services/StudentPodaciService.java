package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.utils.converters.StudentPodaciConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentPodaciService {
	@Autowired
	StudentIndeksRepository studentIndeksRepository;
	
	@Autowired
	StudentPodaciRepository studentPodaciRepository;

	@Autowired
	SlusaPredmetRepository slusaPredmetRepository;


	public StudentPodaci save(StudentPodaciRequest spRequest) {
		StudentPodaci sp = StudentPodaciConverter.toStudentPodaci(spRequest);
		return studentPodaciRepository.save(sp);
	}

	public List<StudentPodaciResponse> findAll() {
		return StudentPodaciConverter.toStudentPodaciResponseList(studentPodaciRepository.findAll());
	}

	public Page<StudentPodaciResponse> findAll(Pageable pageable) {
		Page<StudentPodaci> page = studentPodaciRepository.findAll(pageable);
		return StudentPodaciConverter.toStudentPodaciResponse(page);
	}

	public Optional<StudentPodaciResponse> findById(long id) {
		Optional<StudentPodaci> rez = studentPodaciRepository.findById(id);
		if(rez.isEmpty()) return null;
		return StudentPodaciConverter.toStudentPodaciResponseOptional(rez);
	}

	public Long update(long id, StudentPodaciRequest studentPodaciRequest) {
		StudentPodaci studentPodaci = studentPodaciRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Student sa datim ID ne postoji."));

		studentPodaci.setIme(studentPodaciRequest.getIme());
		studentPodaci.setPrezime(studentPodaciRequest.getPrezime());
		studentPodaci.setSrednjeIme(studentPodaciRequest.getSrednjeIme());
		studentPodaci.setJmbg(studentPodaciRequest.getJmbg());
		studentPodaci.setDatumRodjenja(studentPodaciRequest.getDatumRodjenja());
		studentPodaci.setMestoRodjenja(studentPodaciRequest.getMestoRodjenja());
		studentPodaci.setMestoPrebivalista(studentPodaciRequest.getMestoPrebivalista());
		studentPodaci.setDrzavaRodjenja(studentPodaciRequest.getDrzavaRodjenja());
		studentPodaci.setDrzavljanstvo(studentPodaciRequest.getDrzavljanstvo());
		studentPodaci.setNacionalnost(studentPodaciRequest.getNacionalnost());
		studentPodaci.setPol(studentPodaciRequest.getPol());
		studentPodaci.setAdresa(studentPodaciRequest.getAdresa());
		studentPodaci.setBrojTelefonaMobilni(studentPodaciRequest.getBrojTelefonaMobilni());
		studentPodaci.setBrojTelefonaFiksni(studentPodaciRequest.getBrojTelefonaFiksni());
		studentPodaci.setEmail(studentPodaciRequest.getEmail());
		studentPodaci.setBrojLicneKarte(studentPodaciRequest.getBrojLicneKarte());
		studentPodaci.setLicnuKartuIzdao(studentPodaciRequest.getLicnuKartuIzdao());
		studentPodaci.setMestoStanovanja(studentPodaciRequest.getMestoStanovanja());
		studentPodaci.setAdresaStanovanja(studentPodaciRequest.getAdresaStanovanja());

		StudentPodaci savedStudent = studentPodaciRepository.save(studentPodaci);
		return savedStudent.getId();
	}

	public StudentProfileDTO getStudentProfile(Long indeksId) {
		StudentProfileDTO retVal = new StudentProfileDTO();
		StudentIndeks studIndeks = studentIndeksRepository.findById(indeksId).get();
		retVal.setIndeks(studIndeks);		
		retVal.setSlusaPredmete(slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(indeksId));
		return retVal;
	}
	
	public StudentWebProfileDTO getStudentWebProfile(Long indeksId) {
		StudentWebProfileDTO retVal = new StudentWebProfileDTO();
		StudentIndeks studIndeks = studentIndeksRepository.findById(indeksId).get();
		Long studPodaciId = studIndeks.getStudent().getId();
		retVal.setAktivanIndeks(studentPodaciRepository.getAktivanIndeks(studPodaciId));
		retVal.setSlusaPredmete(slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(indeksId));
		return retVal;
	}

}
