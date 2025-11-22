package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentPodaciConverter {

    public static StudentPodaciResponse toStudentPodaciResponse(StudentPodaci student) {
        if (student == null) return null;

        return new StudentPodaciResponse(
                student.getId(),
                student.getIme(),
                student.getPrezime(),
                student.getSrednjeIme(),
                student.getJmbg(),
                student.getDatumRodjenja(),
                student.getMestoRodjenja(),
                student.getMestoPrebivalista(),
                student.getDrzavaRodjenja(),
                student.getDrzavljanstvo(),
                student.getNacionalnost(),
                student.getPol(),
                student.getAdresa(),
                student.getBrojTelefonaMobilni(),
                student.getBrojTelefonaFiksni(),
                student.getEmail(),
                student.getBrojLicneKarte(),
                student.getLicnuKartuIzdao(),
                student.getMestoStanovanja(),
                student.getAdresaStanovanja()
        );
    }

    //Page
    public static Page<StudentPodaciResponse> toStudentPodaciResponse(Page<StudentPodaci> students) {
        List<StudentPodaciResponse> content = students.getContent()
                .stream()
                .map(StudentPodaciConverter::toStudentPodaciResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(content, students.getPageable(), students.getTotalElements());
    }

    //Lista
    public static List<StudentPodaciResponse> toStudentPodaciResponseList(List<StudentPodaci> students) {
        if (students == null) return null;

        return students.stream()
                .map(StudentPodaciConverter::toStudentPodaciResponse)
                .collect(Collectors.toList());
    }

    public static Optional<StudentPodaciResponse> toStudentPodaciResponseOptional(Optional<StudentPodaci> studentOpt) {
        return studentOpt.map(StudentPodaciConverter::toStudentPodaciResponse);
    }


    public static StudentPodaci toStudentPodaci(StudentPodaciRequest req) {
        if (req == null) return null;

        StudentPodaci student = new StudentPodaci();

        student.setId(req.getId());
        student.setIme(req.getIme());
        student.setPrezime(req.getPrezime());
        student.setSrednjeIme(req.getSrednjeIme());
        student.setJmbg(req.getJmbg());
        student.setDatumRodjenja(req.getDatumRodjenja());
        student.setMestoRodjenja(req.getMestoRodjenja());
        student.setMestoPrebivalista(req.getMestoPrebivalista());
        student.setDrzavaRodjenja(req.getDrzavaRodjenja());
        student.setDrzavljanstvo(req.getDrzavljanstvo());
        student.setNacionalnost(req.getNacionalnost());
        student.setPol(req.getPol());
        student.setAdresa(req.getAdresa());
        student.setBrojTelefonaMobilni(req.getBrojTelefonaMobilni());
        student.setBrojTelefonaFiksni(req.getBrojTelefonaFiksni());
        student.setEmail(req.getEmail());
        student.setBrojLicneKarte(req.getBrojLicneKarte());
        student.setLicnuKartuIzdao(req.getLicnuKartuIzdao());
        student.setMestoStanovanja(req.getMestoStanovanja());
        student.setAdresaStanovanja(req.getAdresaStanovanja());

        return student;
    }


}
