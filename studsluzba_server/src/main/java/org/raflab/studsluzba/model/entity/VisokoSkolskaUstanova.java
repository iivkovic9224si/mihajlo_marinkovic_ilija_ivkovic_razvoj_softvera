package org.raflab.studsluzba.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VisokoSkolskaUstanova that = (VisokoSkolskaUstanova) o;
        return Objects.equals(naziv, that.naziv) && vrsta == that.vrsta;
    }
}


