package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SrednjaSkola {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String naziv;

    @Enumerated(EnumType.STRING)
    private VrstaSrednjeSkole vrstaSrednjeSkole;

    @JsonIgnore
    @OneToMany(mappedBy = "srednjaSkola")
    private List<StudentPodaci> studentPodaci;
}
