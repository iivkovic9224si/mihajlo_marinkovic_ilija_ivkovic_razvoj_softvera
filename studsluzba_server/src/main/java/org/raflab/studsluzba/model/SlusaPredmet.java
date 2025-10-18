package org.raflab.studsluzba.model;

import javax.persistence.*;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Data
public class SlusaPredmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

    @ManyToOne
    private SkolskaGodina skolskaGodina;

	@ManyToOne	
	private StudentIndeks studentIndeks;
	
	@ManyToOne
	private DrziPredmet drziPredmet;

    @JsonIgnore
    @OneToMany(mappedBy = "slusaPredmet")
    private List<PrijavaIspita> prijavaIspita;

}
