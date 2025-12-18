// =====================
// NastavnikZvanjeConverter.java
// =====================
package org.raflab.studsluzba.utils.converters;

import org.raflab.studsluzba.model.entity.NastavnikZvanje;
import request.NastavnikZvanjeRequest;
import response.NastavnikZvanjeResponse;

public class NastavnikZvanjeConverter {
    public static NastavnikZvanje toNastavnikZvanje(NastavnikZvanjeRequest request) {
        NastavnikZvanje nz = new NastavnikZvanje();
        nz.setDatumIzbora(request.getDatumIzbora());
        nz.setNaucnaOblast(request.getNaucnaOblast());
        nz.setUzaNaucnaOblast(request.getUzaNaucnaOblast());
        nz.setZvanje(request.getZvanje());
        nz.setAktivno(request.isAktivno());
        return nz;
    }

    public static NastavnikZvanjeResponse toNastavnikZvanjeResponse(NastavnikZvanje nz) {
        return new NastavnikZvanjeResponse(
                nz.getId(),
                nz.getDatumIzbora(),
                nz.getNaucnaOblast(),
                nz.getUzaNaucnaOblast(),
                nz.getZvanje(),
                nz.isAktivno(),
                org.raflab.studsluzba.utils.converters.NastavnikConverter.toNastavnikResponse(nz.getNastavnik())
        );
    }
}
