package org.raflab.studsluzbadesktopclient.dtos;

public class DrziPredmetResponse {
    private String skolskaGodinaNaziv;
    private String nastavnikEmail;
    private String predmetSifra;

    public String getSkolskaGodinaNaziv() { return skolskaGodinaNaziv; }
    public void setSkolskaGodinaNaziv(String skolskaGodinaNaziv) { this.skolskaGodinaNaziv = skolskaGodinaNaziv; }

    public String getNastavnikEmail() { return nastavnikEmail; }
    public void setNastavnikEmail(String nastavnikEmail) { this.nastavnikEmail = nastavnikEmail; }

    public String getPredmetSifra() { return predmetSifra; }
    public void setPredmetSifra(String predmetSifra) { this.predmetSifra = predmetSifra; }
}

