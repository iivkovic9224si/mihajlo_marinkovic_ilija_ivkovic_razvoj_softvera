package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.entity.StudentIndeks;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.utils.converters.StudentPodaciConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import request.StudentPodaciRequest;
import response.StudentPodaciResponse;

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

    @Transactional
    public StudentPodaci save(StudentPodaciRequest spRequest) {
        // Prevencija dodavanja studenta sa istim JMBG-om
        Optional<StudentPodaci> existingStudent = studentPodaciRepository.findByJmbg(spRequest.getJmbg());
        if (existingStudent.isPresent()) {
            throw new IllegalArgumentException("Student sa JMBG-om " + spRequest.getJmbg() + " već postoji u sistemu.");
        }

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
        if(rez.isEmpty()) return Optional.empty();
        return StudentPodaciConverter.toStudentPodaciResponseOptional(rez);
    }

    public Optional<StudentPodaciResponse> findByJmbg(String jmbg) {
        Optional<StudentPodaci> rez = studentPodaciRepository.findByJmbg(jmbg);
        if(rez.isEmpty()) return Optional.empty();
        return StudentPodaciConverter.toStudentPodaciResponseOptional(rez);
    }

    public Long update(long id, StudentPodaciRequest studentPodaciRequest) {
        StudentPodaci studentPodaci = studentPodaciRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student sa datim ID ne postoji."));

        // Provera da li je JMBG promenjen i da li novi JMBG već postoji
        if (!studentPodaci.getJmbg().equals(studentPodaciRequest.getJmbg())) {
            Optional<StudentPodaci> existingWithNewJmbg = studentPodaciRepository.findByJmbg(studentPodaciRequest.getJmbg());
            if (existingWithNewJmbg.isPresent() && !existingWithNewJmbg.get().getId().equals(id)) {
                throw new IllegalArgumentException("Student sa JMBG-om " + studentPodaciRequest.getJmbg() + " već postoji u sistemu.");
            }
        }

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

    @Transactional
    public void deleteStudentPodaci(StudentPodaciRequest studentPodaciRequest) {
        StudentPodaci studentPodaci = studentPodaciRepository.findByJmbg(studentPodaciRequest.getJmbg())
                .orElseThrow(() -> new RuntimeException("StudentPodaci sa JMBG " + studentPodaciRequest.getJmbg() + " nije pronađen."));

        try {
            studentPodaciRepository.delete(studentPodaci);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "Brisanje nije moguće – StudentPodaci je povezan sa drugim entitetima (npr. StudentIndeks, predmeti).", e);
        }
    }

    // Nova metoda za pronalaženje ID-a studenta preko JMBG-a
    public Long findStudentIdByJmbg(String jmbg) {
        return studentPodaciRepository.findByJmbg(jmbg)
                .map(StudentPodaci::getId)
                .orElseThrow(() -> new IllegalArgumentException("Student sa JMBG-om " + jmbg + " nije pronađen."));
    }

    // Nova metoda za proveru postojanja studenta preko JMBG-a
    public boolean existsByJmbg(String jmbg) {
        return studentPodaciRepository.findByJmbg(jmbg).isPresent();
    }
}