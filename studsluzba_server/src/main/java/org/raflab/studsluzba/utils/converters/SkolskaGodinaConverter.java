    package org.raflab.studsluzba.utils.converters;

    import org.raflab.studsluzba.model.entity.SkolskaGodina;
    import request.SkolskaGodinaRequest;
    import response.SkolskaGodinaResponse;

    import java.util.ArrayList;
    import java.util.List;

    public class SkolskaGodinaConverter {
        public static SkolskaGodina toSkolskaGodina(SkolskaGodinaRequest request) {
            SkolskaGodina godina = new SkolskaGodina();
            godina.setGodina(request.getGodina());
            godina.setAktivna(request.isAktivna());
            return godina;
        }

        public static SkolskaGodinaResponse toSkolskaGodinaResponse(SkolskaGodina godina) {
            SkolskaGodinaResponse response = new SkolskaGodinaResponse();
            response.setGodina(godina.getGodina());
            response.setAktivna(godina.isAktivna());
            return response;
        }

        public static List<SkolskaGodinaResponse> toSkolskaGodinaResponseList(List<SkolskaGodina> godinaList) {
            List<SkolskaGodinaResponse> responseList = new ArrayList<>();
            for (SkolskaGodina g : godinaList) {
                responseList.add(toSkolskaGodinaResponse(g));
            }
            return responseList;
        }
    }
