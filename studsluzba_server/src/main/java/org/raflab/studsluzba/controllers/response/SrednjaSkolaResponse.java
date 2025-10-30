package org.raflab.studsluzba.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.raflab.studsluzba.model.entity.VrstaSrednjeSkole;

import javax.annotation.sql.DataSourceDefinition;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SrednjaSkolaResponse {
    @NotNull
    private String sifra;
    private String naziv;
    private VrstaSrednjeSkole vrstaSrednjeSkole;
}
