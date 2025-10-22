package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class VisokoSkolskaUstanova {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String naziv;

    @Enumerated(EnumType.STRING)
    private VrstaVisokoSkolskihUstanova vrsta;

    @JsonIgnore
    @OneToMany(mappedBy = "visokoSkolskaUstanova")
    private List<StudentPodaci> studentPodaci;

    @ManyToMany
    private List<Nastavnik>  nastavnik;

    @JsonIgnore
    @OneToMany(mappedBy = "priznatoOdInstitucije")
    private List<PolozenPredmet> polozenPredmet;
}
