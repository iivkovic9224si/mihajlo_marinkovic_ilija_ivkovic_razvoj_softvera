package org.raflab.studsluzba;

import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.*;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private NastavnikZvanjeRepository nastavnikZvanjeRepository;
    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private GrupaRepository grupaRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired
    private OstvareniPredispitniRepository ostvareniPredispitniRepository;
    @Autowired
    private PredispitneObavezeRepository predispitneObavezeRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired
    private StudijskiTokAktivnostiRepository studijskiTokAktivnostiRepository;
    @Autowired
    private PolozenPredmetRepository polozenPredmetRepository;
    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private VisokoSkolskaUstanovaRepository visokoSkolskaUstanovaRepository;
    @Autowired
    private SrednjaSkolaRepository srednjaSkolaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (studijskiProgramRepository.count() > 0) {
            return;
        }

        // ----------- Studijski programi -----------
        List<StudijskiProgram> spList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudijskiProgram sp = new StudijskiProgram();
            sp.setOznaka("SP" + i);
            sp.setNaziv("Program " + i);
            sp.setGodinaAkreditacije(2020 + i);
            sp.setZvanje("Zvanje " + i);
            sp.setTrajanjeGodina(4);
            sp.setTrajanjeSemestara(8);
            sp.setVrstaStudija("OAS");
            sp.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp));
        }

        // ----------- Predmeti -----------
        List<Predmet> predmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Predmet p = new Predmet();
            p.setSifra("PR" + i);
            p.setNaziv("Predmet " + i);
            p.setOpis("Opis predmeta " + i);
            p.setEspb(6 + i);
            p.setStudProgram(spList.get((i - 1) % spList.size()));
            p.setObavezan(i % 2 == 0);
            predmetList.add(predmetRepository.save(p));
        }

        // ----------- Nastavnici -----------
        List<Nastavnik> nastavnikList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Nastavnik n = new Nastavnik();
            n.setIme("Nastavnik" + i);
            n.setPrezime("Prezime" + i);
            n.setSrednjeIme("Srednje" + i);
            n.setEmail("nastavnik" + i + "@example.com");
            n.setBrojTelefona("06012345" + i);
            n.setAdresa("Adresa " + i);
            n.setDatumRodjenja(LocalDate.of(1980 + i, i, i));
            n.setPol(i % 2 == 0 ? 'M' : 'F');
            n.setJmbg("80010123456" + i);
            nastavnikList.add(nastavnikRepository.save(n));
        }

        // ----------- Nastavničko zvanje -----------
        for (int i = 1; i <= 5; i++) {
            NastavnikZvanje nz = new NastavnikZvanje();
            nz.setDatumIzbora(LocalDate.of(2020 + i, i, i));
            nz.setNaucnaOblast("Oblast " + i);
            nz.setUzaNaucnaOblast("Uza oblast " + i);
            nz.setZvanje("Zvanje " + i);
            nz.setAktivno(i % 2 == 0);
            nz.setNastavnik(nastavnikList.get(i - 1));
            nastavnikZvanjeRepository.save(nz);
        }

        // ----------- StudentPodaci -----------
        List<StudentPodaci> studentPodaciList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentPodaci s = new StudentPodaci();
            s.setIme("Student" + i);
            s.setPrezime("Prezime" + i);
            s.setSrednjeIme("Srednje" + i);
            s.setJmbg("00101012345" + i);
            s.setDatumRodjenja(LocalDate.of(2000 + i, i, i));
            s.setMestoRodjenja("Mesto" + i);
            s.setMestoPrebivalista("Prebivaliste" + i);
            s.setDrzavaRodjenja("Srbija");
            s.setDrzavljanstvo("Srbija");
            s.setNacionalnost("Srpska");
            s.setPol(i % 2 == 0 ? 'F' : 'M');
            s.setAdresa("Adresa " + i);
            s.setBrojTelefonaMobilni("06123456" + i);
            s.setEmail("student" + i + "@example.com");
            studentPodaciList.add(studentPodaciRepository.save(s));
        }

        // ----------- StudentIndeks -----------
        for (int i = 1; i <= 5; i++) {
            StudentIndeks si = new StudentIndeks();
            si.setBroj(i);
            si.setGodina(2023);
            si.setStudProgramOznaka(spList.get(i - 1).getOznaka());
            si.setNacinFinansiranja(i % 2 == 0 ? "Budzet" : "Samofinansiranje");
            si.setAktivan(true);
            si.setVaziOd(LocalDate.of(2023, 10, i));
            si.setStudent(studentPodaciList.get(i - 1));
            si.setStudijskiProgram(spList.get(i - 1));
            si.setOstvarenoEspb(0);
            studentIndeksRepository.save(si);
        }

        // ----------- DrziPredmet & SlusaPredmet -----------
        for (int i = 0; i < 5; i++) {
            DrziPredmet dp = new DrziPredmet();
            dp.setNastavnik(nastavnikList.get(i));
            dp.setPredmet(predmetList.get(i));
            drziPredmetRepository.save(dp);

            SlusaPredmet sl = new SlusaPredmet();
            sl.setDrziPredmet(dp);
            sl.setStudentIndeks(studentIndeksRepository.findAll().get(i));
            slusaPredmetRepository.save(sl);
        }

        // ----------- Grupe -----------
        for (int i = 0; i < 5; i++) {
            Grupa g = new Grupa();
            g.setStudijskiProgram(spList.get(i));
            g.setPredmeti(Collections.singletonList(predmetList.get(i)));
            grupaRepository.save(g);
        }

        // ----------- VisokoSkolskeUstanove (primera) -----------
        List<VisokoSkolskaUstanova> vsuList = new ArrayList<>();

        vsuList.add(new VisokoSkolskaUstanova() {{
            setNaziv("Univerzitet Beograd");
            setVrsta(VrstaVisokoSkolskihUstanova.UNIVERZITET);
        }});
        vsuList.add(new VisokoSkolskaUstanova() {{
            setNaziv("Univerzitet Novi Sad");
            setVrsta(VrstaVisokoSkolskihUstanova.UNIVERZITET);
        }});
        vsuList.add(new VisokoSkolskaUstanova() {{
            setNaziv("Visoka Tehnička Škola Niš");
            setVrsta(VrstaVisokoSkolskihUstanova.VISOKA_TEHNICKA);
        }});
        vsuList.add(new VisokoSkolskaUstanova() {{
            setNaziv("Visoka Ekonomksa Škola Subotica");
            setVrsta(VrstaVisokoSkolskihUstanova.VISOKA_POSLOVNA);
        }});

        int trenutnaGodina = LocalDate.now().getYear();
        for (int godina = 2016; godina <= trenutnaGodina; godina++) {
            SkolskaGodina sg = new SkolskaGodina();
            sg.setGodina(godina + "/" + (godina + 1));
            sg.setAktivna(godina == trenutnaGodina); // samo tekuća godina je aktivna
            skolskaGodinaRepository.save(sg);
        }
    }
}
