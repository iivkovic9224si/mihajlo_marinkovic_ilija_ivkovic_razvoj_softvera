package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.model.entity.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
public class SlusaPredmetResponse {

    private String skolskaGodina;
    private String studentIndeks;
    private String nastavnik;
    private String predmet;

}
