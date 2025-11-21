package org.raflab.studsluzba.controllers.dtosIlija;

import java.util.List;

import lombok.Data;
import org.raflab.studsluzba.model.entity.Predmet;
import org.raflab.studsluzba.model.entity.SlusaPredmet;
import org.raflab.studsluzba.model.entity.StudentIndeks;

@Data
public class StudentProfileDTO {
	
	private StudentIndeks indeks;	

	
	// za aktivnu skolsku godinu
	
	private List<SlusaPredmet> slusaPredmete;
	
	private List<Predmet> nepolozeniPredmeti;
	
}
