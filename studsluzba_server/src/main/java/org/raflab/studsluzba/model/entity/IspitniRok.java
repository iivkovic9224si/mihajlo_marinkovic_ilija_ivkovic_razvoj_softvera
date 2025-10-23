package org.raflab.studsluzba.model.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class IspitniRok {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate pocetak;
    private LocalDate kraj;

    @JsonIgnore
    @OneToMany(mappedBy = "ispitniRok")
    private List<Ispit> ispit;

    @ManyToOne
    private SkolskaGodina skolskaGodina;

}
