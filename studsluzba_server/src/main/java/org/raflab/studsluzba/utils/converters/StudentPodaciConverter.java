package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
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

}
