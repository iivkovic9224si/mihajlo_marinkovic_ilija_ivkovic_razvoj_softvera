package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.controllers.response.RezultatiIspitaResponse;

public class IspitConverter {
    public static RezultatiIspitaResponse toRezultatiIspitaDTO(Object[] row){
        return new RezultatiIspitaResponse(
                (String) row[0],  // ime
                (String) row[1],  // prezime
                (String) row[2],  // studProgramOznaka
                (Integer) row[3], // godina
                (Integer) row[4], // broj
                (Double) row[5]   // ukupnoPoena
        );
    }
}
