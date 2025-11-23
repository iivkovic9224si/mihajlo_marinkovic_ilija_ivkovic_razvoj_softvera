package org.raflab.studsluzba.model.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SkolskaGodina {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private boolean aktivna;
    private String godina;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<SlusaPredmet> slusaPredmet;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<DrziPredmet> drziPredmet;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<IspitniRok> ispitniRok;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<Uplata> uplate;

}
