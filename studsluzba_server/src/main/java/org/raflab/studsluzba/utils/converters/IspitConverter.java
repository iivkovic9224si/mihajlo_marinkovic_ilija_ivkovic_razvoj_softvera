package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.dtos.RezultatiIspitaDTO;

public class IspitConverter {
    public static RezultatiIspitaDTO toRezultatiIspitaDTO(Object[] row){
        return new RezultatiIspitaDTO(
                (String) row[0],  // ime
                (String) row[1],  // prezime
                (String) row[2],  // studProgramOznaka
                (Integer) row[3], // godina
                (Integer) row[4], // broj
                (Double) row[5]   // ukupnoPoena
        );
    }
}
