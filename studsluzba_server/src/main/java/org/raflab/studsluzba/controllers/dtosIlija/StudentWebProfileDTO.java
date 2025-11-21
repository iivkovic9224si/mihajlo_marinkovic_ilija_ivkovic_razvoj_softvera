package org.raflab.studsluzba.controllers.dtosIlija;

import java.util.List;

import lombok.Data;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;

@Data
public class StudentWebProfileDTO {
	
	private StudentIndeks aktivanIndeks;	

	// za aktivnu skolsku godinu
	private List<SlusaPredmet> slusaPredmete;
	
}
