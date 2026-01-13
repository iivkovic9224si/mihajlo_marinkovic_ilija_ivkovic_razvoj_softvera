package org.raflab.studsluzba.model.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"studProgram"})
public class Predmet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String sifra;
	private String naziv;
	private String opis;
	private Integer espb;
    private Integer godinaStudija;

	@ManyToOne
	private StudijskiProgram studProgram;

	private boolean obavezan;

    @OneToMany(mappedBy = "predmet")
    private List<PredispitneObaveze> predispitneObaveze;

    @ManyToMany(mappedBy = "predmet")
    private List<StudijskiTokAktivnosti> studijskiTokAktivnosti;

    @ManyToMany(mappedBy = "predmeti")
    private List<Grupa> grupa;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sifra == null) ? 0 : sifra.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predmet other = (Predmet) obj;
		if (sifra == null) {
			if (other.sifra != null)
				return false;
		} else if (!sifra.equals(other.sifra))
			return false;
		return true;
	}

}
