package org.raflab.studsluzba.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import org.raflab.studsluzba.model.entity.StudentPodaci;
import org.raflab.studsluzba.model.entity.VrstaSrednjeSkole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class SrednjaSkolaRequest {
    @NotNull
    private String naziv;
    @NotNull
    private VrstaSrednjeSkole vrstaSrednjeSkole;
}
