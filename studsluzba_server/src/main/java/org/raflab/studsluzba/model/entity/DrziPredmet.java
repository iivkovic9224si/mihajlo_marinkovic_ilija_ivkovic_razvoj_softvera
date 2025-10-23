package org.raflab.studsluzba.model.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class DrziPredmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    private SkolskaGodina skolskaGodina;

	@ManyToOne
	private Nastavnik nastavnik;
	
	@ManyToOne
	private Predmet predmet;

    @JsonIgnore
    @OneToMany(mappedBy = "drziPredmet")
    private List<Ispit> ispit;
}
