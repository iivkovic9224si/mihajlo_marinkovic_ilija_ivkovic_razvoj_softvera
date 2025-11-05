package org.raflab.studsluzba;

import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

        boolean tabelePrazne =
                studijskiProgramRepository.count() == 0 &&
                        predmetRepository.count() == 0 &&
                        nastavnikRepository.count() == 0 &&
                        nastavnikZvanjeRepository.count() == 0 &&
                        studentPodaciRepository.count() == 0 &&
                        studentIndeksRepository.count() == 0 &&
                        drziPredmetRepository.count() == 0 &&
                        slusaPredmetRepository.count() == 0 &&
                        grupaRepository.count() == 0 &&
                        skolskaGodinaRepository.count() == 0 &&
                        ostvareniPredispitniRepository.count() == 0 &&
                        predispitneObavezeRepository.count() == 0 &&
                        ispitniRokRepository.count() == 0 &&
                        ispitRepository.count() == 0 &&
                        prijavaIspitaRepository.count() == 0 &&
                        studijskiTokAktivnostiRepository.count() == 0 &&
                        polozenPredmetRepository.count() == 0 &&
                        izlazakNaIspitRepository.count() == 0 &&
                        visokoSkolskaUstanovaRepository.count() == 0 &&
                        srednjaSkolaRepository.count() == 0;

        if (!tabelePrazne) {
            return;
        }

        List<StudijskiProgram> spList = new ArrayList<>();
        if (studijskiProgramRepository.count() == 0) {


            // 1. RN - Racunarske Nauke
            StudijskiProgram sp1 = new StudijskiProgram();
            sp1.setOznaka("RN");
            sp1.setNaziv("Racunarske Nauke");
            sp1.setGodinaAkreditacije(2016); // izmislimo godinu ranije
            sp1.setZvanje("Diplomirani inzenjer racunarskih nauka");
            sp1.setTrajanjeGodina(4);
            sp1.setTrajanjeSemestara(8);
            sp1.setVrstaStudija("OAS");
            sp1.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp1));

            // 2. RI - Racunarsko Inzenjerstvo
            StudijskiProgram sp2 = new StudijskiProgram();
            sp2.setOznaka("RI");
            sp2.setNaziv("Racunarsko Inzenjerstvo");
            sp2.setGodinaAkreditacije(2017); // izmislimo godinu ranije
            sp2.setZvanje("Diplomirani inzenjer racunarskog inzenjerstva");
            sp2.setTrajanjeGodina(4);
            sp2.setTrajanjeSemestara(8);
            sp2.setVrstaStudija("OAS");
            sp2.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp2));

            // 3. SOFT - Softversko Inzenjerstvo
            StudijskiProgram sp3 = new StudijskiProgram();
            sp3.setOznaka("SI");
            sp3.setNaziv("Softversko Inzenjerstvo");
            sp3.setGodinaAkreditacije(2022); // recimo kasnije
            sp3.setZvanje("Diplomirani inzenjer softverskog inzenjerstva");
            sp3.setTrajanjeGodina(4);
            sp3.setTrajanjeSemestara(8);
            sp3.setVrstaStudija("OAS");
            sp3.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp3));

            // 4. IT - Informacione Tehnologije (OSS)
            StudijskiProgram sp4 = new StudijskiProgram();
            sp4.setOznaka("IT");
            sp4.setNaziv("Informacione Tehnologije");
            sp4.setGodinaAkreditacije(2020); // izmislimo godinu
            sp4.setZvanje("Strukovni dipl. inzenjer IT");
            sp4.setTrajanjeGodina(3);
            sp4.setTrajanjeSemestara(6);
            sp4.setVrstaStudija("OSS");
            sp4.setUkupnoEspb(180);
            spList.add(studijskiProgramRepository.save(sp4));
        }

        // ----------- Predmeti -----------
        List<Predmet> predmetList = new ArrayList<>();
        if(predmetRepository.count() == 0) {

            int counter = 1;

            Map<String, String[]> realniPredmeti = new HashMap<>();
            realniPredmeti.put("RN", new String[]{
                    "Osnove programiranja",
                    "Algoritmi i strukture podataka",
                    "Računarske mreže",
                    "Baze podataka",
                    "Softversko inženjerstvo"
            });
            realniPredmeti.put("RI", new String[]{
                    "Matematika 1",
                    "Matematika 2",
                    "Fizika",
                    "Elektronika",
                    "Operativni sistemi"
            });
            realniPredmeti.put("SI", new String[]{ // OVDJE JE KLJUČ POZOVAN PO OZNACI SP
                    "Objektno-orijentisano programiranje",
                    "Razvoj web aplikacija",
                    "Softverski projekti",
                    "Analiza i dizajn softvera",
                    "Testiranje softvera"
            });
            realniPredmeti.put("IT", new String[]{
                    "Uvod u informacione tehnologije",
                    "Mrežne tehnologije",
                    "Programiranje u Javi",
                    "Baze podataka i SQL",
                    "Računarska sigurnost"
            });

            Map<String, String[]> opisiPredmeta = new HashMap<>();
            opisiPredmeta.put("RN", new String[]{
                    "Upoznavanje sa osnovnim principima programiranja u Pythonu i Javi.",
                    "Proučavanje osnovnih algoritama i struktura podataka.",
                    "Osnove računarskih mreža, topologije i protokoli.",
                    "Upoznavanje sa konceptima baza podataka i SQL jezikom.",
                    "Principi softverskog inženjerstva i životni ciklus softvera."
            });
            opisiPredmeta.put("RI", new String[]{
                    "Osnovni koncepti diferencijalne i integralne matematike.",
                    "Napredna matematika sa linearnom algebrom i diskretnom matematikom.",
                    "Fizički zakoni i njihova primena u računarstvu.",
                    "Uvod u elektroniku i digitalne logičke sklopove.",
                    "Principi i koncepti operativnih sistema."
            });
            opisiPredmeta.put("SI", new String[]{ // OVDJE JE KLJUČ POZOVAN PO OZNACI SP
                    "Programiranje koristeći objektno-orijentisane paradigme.",
                    "Razvoj web aplikacija koristeći moderne tehnologije.",
                    "Praktični softverski projekti u timskom okruženju.",
                    "Analiza zahteva i dizajn softverskih rešenja.",
                    "Metode i alati za testiranje softvera."
            });
            opisiPredmeta.put("IT", new String[]{
                    "Uvod u osnovne informacione tehnologije i softver.",
                    "Osnove mrežnih tehnologija i TCP/IP protokola.",
                    "Programiranje koristeći jezik Java.",
                    "Rad sa relacijskim bazama podataka i SQL upiti.",
                    "Osnovi računarske sigurnosti i zaštite podataka."
            });

            for (StudijskiProgram sp : spList) {
                String[] nazivi = realniPredmeti.get(sp.getOznaka());
                String[] opisi = opisiPredmeta.get(sp.getOznaka());

                for (int i = 0; i < 5; i++) {
                    Predmet p = new Predmet();
                    p.setSifra("PR" + counter++);
                    p.setNaziv(nazivi[i]);
                    p.setOpis(opisi[i]);
                    p.setEspb((i % 2 == 0) ? 6 : 8); // varira ESPB
                    p.setObavezan(p.getEspb() == 6); // uglavnom 6 ESPB obavezni
                    p.setStudProgram(sp);
                    predmetList.add(predmetRepository.save(p));
                }
            }
        }

        // ----------- Visokoškolske ustanove -----------
        List<VisokoSkolskaUstanova> vsuList = new ArrayList<>();
        String[] naziviVSU = {
                "Univerzitet u Beogradu",
                "Univerzitet u Novom Sadu",
                "Univerzitet u Nišu",
                "Univerzitet u Kragujevcu",
                "Univerzitet umetnosti Beograd",
                "Visoka tehnička škola Subotica",
                "Visoka poslovna škola Novi Sad",
                "Visoka medicinska škola Beograd",
                "Fakultet tehničkih nauka Novi Sad",
                "Fakultet političkih nauka Beograd"
        };
        VrstaVisokoSkolskihUstanova[] vrste = {
                VrstaVisokoSkolskihUstanova.UNIVERZITET,
                VrstaVisokoSkolskihUstanova.UNIVERZITET,
                VrstaVisokoSkolskihUstanova.UNIVERZITET,
                VrstaVisokoSkolskihUstanova.UNIVERZITET,
                VrstaVisokoSkolskihUstanova.VISOKA_UMETNOSTI,
                VrstaVisokoSkolskihUstanova.VISOKA_TEHNICKA,
                VrstaVisokoSkolskihUstanova.VISOKA_POSLOVNA,
                VrstaVisokoSkolskihUstanova.VISOKA_MEDICINSKA,
                VrstaVisokoSkolskihUstanova.FAKULTET,
                VrstaVisokoSkolskihUstanova.FAKULTET
        };

        if(visokoSkolskaUstanovaRepository.count() == 0) {
            for(int i = 0; i < naziviVSU.length; i++) {
                VisokoSkolskaUstanova vsu = new VisokoSkolskaUstanova();
                vsu.setSifra("VS" + (i + 1));
                vsu.setNaziv(naziviVSU[i]);
                vsu.setVrsta(vrste[i]);
                visokoSkolskaUstanovaRepository.save(vsu);
            }
        }



        // ----------- Nastavnici i njihova zvanja -----------
        List<Nastavnik> nastavnikList = new ArrayList<>();
        List<NastavnikZvanje> sveZvanjeList = new ArrayList<>();

        String[] imena = {"Marko", "Jovana", "Petar", "Ana", "Stefan", "Milica", "Nikola", "Ivana", "Milan", "Tamara"};
        String[] prezimena = {"Jovanović", "Petrović", "Nikolić", "Stojanović", "Marković", "Popović", "Kovačević", "Đorđević", "Ristić", "Lukić"};
        String[] zvanja = {"Asistent", "Docent", "Vanredni profesor", "Redovni profesor", "Stručni saradnik"};
        String[] oblasti = {"Računarske nauke", "Softversko inženjerstvo", "Informacione tehnologije", "Matematika", "Fizika"};

        for(int i = 0; i < 10; i++) {
            Nastavnik n = new Nastavnik();
            n.setIme(imena[i]);
            n.setPrezime(prezimena[i]);
            n.setSrednjeIme("Srednje" + i);
            n.setEmail(imena[i].toLowerCase() + "." + prezimena[i].toLowerCase() + "@example.com");
            n.setBrojTelefona("06012345" + i);
            n.setAdresa("Adresa " + i);
            n.setDatumRodjenja(LocalDate.of(1975 + i%10, (i%12)+1, (i%28)+1));
            n.setPol(i % 2 == 0 ? 'M' : 'F');
            n.setJmbg("75010123456" + i);

            // Sačuvamo nastavnika
            nastavnikList.add(nastavnikRepository.save(n));

            // Generišemo 1 do 3 zvanja po nastavniku
            int brojZvanja = 1 + new Random().nextInt(3);
            boolean aktivnoPostavljeno = false;

            for(int j = 0; j < brojZvanja; j++) {
                NastavnikZvanje nz = new NastavnikZvanje();
                nz.setNastavnik(n);
                nz.setDatumIzbora(LocalDate.of(2000 + j + i, (j%12)+1, (j%28)+1));
                nz.setNaucnaOblast(oblasti[(i+j) % oblasti.length]);
                nz.setUzaNaucnaOblast(oblasti[(i+j+1) % oblasti.length]);
                nz.setZvanje(zvanja[(i+j) % zvanja.length]);

                // Postavimo samo jedno aktivno
                if(!aktivnoPostavljeno) {
                    nz.setAktivno(true);
                    aktivnoPostavljeno = true;
                } else {
                    nz.setAktivno(false);
                }

                sveZvanjeList.add(nastavnikZvanjeRepository.save(nz));
            }
        }

        // 1. Pronađi tekuću godinu
        int trenutnaGodina = LocalDate.now().getYear();
        String nazivTekuceGodine = trenutnaGodina + "/" + (trenutnaGodina + 1);

// 2. Poništi sve stare aktivne godine pre nego što postaviš novu
        skolskaGodinaRepository.deactivateAll();

// 3. Prođi kroz sve godine od 2016. do tekuće
        for (int godina = 2016; godina <= trenutnaGodina; godina++) {
            String nazivGodine = godina + "/" + (godina + 1);

            Optional<SkolskaGodina> postojiGodina = skolskaGodinaRepository.findOneByGodina(nazivGodine);
            SkolskaGodina sg;

            if (postojiGodina.isPresent()) {
                sg = postojiGodina.get();
            } else {
                sg = new SkolskaGodina();
                sg.setGodina(nazivGodine);
            }

            // Postavi aktivno samo ako je tekuća godina
            sg.setAktivna(nazivGodine.equals(nazivTekuceGodine));
            skolskaGodinaRepository.save(sg);
        }


        // ----------- DrziPredmet -----------
        if (drziPredmetRepository.count() == 0) {

            List<Nastavnik> nastavnici = nastavnikRepository.findAll();
            List<Predmet> predmeti = (List<Predmet>) predmetRepository.findAll();
            List<SkolskaGodina> skoleGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();

            // Pronađi aktivnu školsku godinu
            SkolskaGodina aktuelnaGodina = null;
            for (SkolskaGodina sg : skoleGodine) {
                if (sg.isAktivna()) {
                    aktuelnaGodina = sg;
                    break;
                }
            }
            if (aktuelnaGodina == null && !skoleGodine.isEmpty()) {
                aktuelnaGodina = skoleGodine.get(0); // fallback
            }

            if (!nastavnici.isEmpty() && !predmeti.isEmpty() && aktuelnaGodina != null) {

                // 1. Svaki nastavnik drži barem jedan predmet
                for (int i = 0; i < nastavnici.size(); i++) {
                    Nastavnik n = nastavnici.get(i);
                    Predmet p = predmeti.get(i % predmeti.size()); // ciklično kroz predmete
                    DrziPredmet dp = new DrziPredmet();
                    dp.setNastavnik(n);
                    dp.setPredmet(p);
                    dp.setSkolskaGodina(aktuelnaGodina);
                    drziPredmetRepository.save(dp);
                }

                // 2. Svaki predmet ima barem jednog nastavnika (dodaj još nastavnika ako treba)
                for (int i = 0; i < predmeti.size(); i++) {
                    Predmet p = predmeti.get(i);
                    Nastavnik n1 = nastavnici.get(i % nastavnici.size());
                    Nastavnik n2 = nastavnici.get((i + 1) % nastavnici.size()); // drugi nastavnik za isti predmet
                    // Proveri da li je dp već postoji za p i n1
                    boolean exists = false;
                    for (DrziPredmet dp : drziPredmetRepository.findAll()) {
                        if (dp.getPredmet().getId().equals(p.getId()) && dp.getNastavnik().getId().equals(n1.getId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        DrziPredmet dp1 = new DrziPredmet();
                        dp1.setPredmet(p);
                        dp1.setNastavnik(n1);
                        dp1.setSkolskaGodina(aktuelnaGodina);
                        drziPredmetRepository.save(dp1);
                    }

                    // Dodaj još jednog nastavnika za isti predmet (da vidimo više nastavnika po predmetu)
                    DrziPredmet dp2 = new DrziPredmet();
                    dp2.setPredmet(p);
                    dp2.setNastavnik(n2);
                    dp2.setSkolskaGodina(aktuelnaGodina);
                    drziPredmetRepository.save(dp2);
                }
            }
        }
        // ----------- Grupa -----------
        if (grupaRepository.count() == 0) {

            if (!spList.isEmpty()) {
                // Grupa 1
                Grupa g1 = new Grupa();
                g1.setStudijskiProgram(spList.get(0));
                grupaRepository.save(g1);

                // Grupa 2
                Grupa g2 = new Grupa();
                g2.setStudijskiProgram(spList.get(1));
                grupaRepository.save(g2);

                // Grupa 3
                Grupa g3 = new Grupa();
                g3.setStudijskiProgram(spList.get(2));
                grupaRepository.save(g3);

                // Grupa 4
                Grupa g4 = new Grupa();
                g4.setStudijskiProgram(spList.get(3));
                grupaRepository.save(g4);
            }
        }

        // ----------- Ispitni rokovi -----------
        if (ispitniRokRepository.count() == 0) {
            List<SkolskaGodina> skoleGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();

            for (SkolskaGodina sg : skoleGodine) {
                List<IspitniRok> rokovi = new ArrayList<>();

                // Jan: 15.01 - 31.01
                IspitniRok jan = new IspitniRok();
                jan.setSkolskaGodina(sg);
                jan.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 1, 15));
                jan.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 1, 31));
                rokovi.add(jan);

                // Feb: 15.02 - 28.02
                IspitniRok feb = new IspitniRok();
                feb.setSkolskaGodina(sg);
                feb.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 2, 15));
                feb.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 2, 28));
                rokovi.add(feb);

                // Jun: 01.06 - 15.06
                IspitniRok jun = new IspitniRok();
                jun.setSkolskaGodina(sg);
                jun.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 6, 1));
                jun.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 6, 15));
                rokovi.add(jun);

                // Jul: 01.07 - 15.07
                IspitniRok jul = new IspitniRok();
                jul.setSkolskaGodina(sg);
                jul.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 7, 1));
                jul.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 7, 15));
                rokovi.add(jul);

                // Avgust: 01.08 - 15.08
                IspitniRok avg = new IspitniRok();
                avg.setSkolskaGodina(sg);
                avg.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 8, 1));
                avg.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 8, 15));
                rokovi.add(avg);

                // Septembar: 01.09 - 15.09
                IspitniRok sep = new IspitniRok();
                sep.setSkolskaGodina(sg);
                sep.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 9, 1));
                sep.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 9, 15));
                rokovi.add(sep);

                // Sačuvaj sve rokove
                for (IspitniRok rok : rokovi) {
                    ispitniRokRepository.save(rok);
                }
            }
        }
        // ----------- Dodavanje više ispita -----------
// Dodajemo 15 ispita za aktivnu školsku godinu i postojeće DrziPredmet
        if (ispitRepository.count() == 0) {

            // 1. Pronađi aktivnu školsku godinu
            List<SkolskaGodina> sveGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();
            SkolskaGodina aktuelnaGodina = null;
            for (SkolskaGodina sg : sveGodine) {
                if (sg.isAktivna()) {
                    aktuelnaGodina = sg;
                    break;
                }
            }

            if (aktuelnaGodina != null) {

                // 2. Pronađi sve DrziPredmet iz te godine
                List<DrziPredmet> sviDp = (List<DrziPredmet>) drziPredmetRepository.findAll();
                List<DrziPredmet> dpAktuelnaGodina = new ArrayList<>();
                for (DrziPredmet dp : sviDp) {
                    if (dp.getSkolskaGodina().getId().equals(aktuelnaGodina.getId())) {
                        dpAktuelnaGodina.add(dp);
                    }
                }

                // 3. Dodaj 15 ispita ciklično kroz dostupne DrziPredmet
                for (int i = 0; i < 15; i++) {
                    DrziPredmet dpZaIspit = dpAktuelnaGodina.get(i % dpAktuelnaGodina.size());

                    Ispit ispit = new Ispit();
                    ispit.setDrziPredmet(dpZaIspit);
                    ispit.setDatumIspita(LocalDate.now().plusDays(3 + i)); // različiti datumi
                    ispit.setVremePocetkaIspita(LocalTime.of(9 + (i % 5), 0));   // menjamo vreme


                    ispitRepository.save(ispit);
                }
            }
        }



        // ----------- Srednje škole -----------
        List<SrednjaSkola> srednjeSkoleList = new ArrayList<>();
        String[] naziviSS = {
                "Gimnazija Nikola Tesla Beograd",
                "Tehnička škola Mihajlo Pupin Novi Sad",
                "Ekonomska škola Subotica",
                "Medicinska škola Niš",
                "Umetnička škola Beograd",
                "Poljoprivredna škola Sombor",
                "Mašinska škola Kragujevac",
                "Gimnazija Jovan Jovanović Zmaj Novi Sad",
                "Tehnička škola Zaječar",
                "Ekonomska škola Beograd",
                "Umetnička škola Novi Sad",
                "Poljoprivredna škola Pančevo",
                "Mašinska škola Subotica",
                "Gimnazija Svetozar Marković Niš",
                "Tehnička škola Kruševac"
        };
        VrstaSrednjeSkole[] vrsteSS = {
                VrstaSrednjeSkole.GIMNAZIJA,
                VrstaSrednjeSkole.STRUCNA,
                VrstaSrednjeSkole.STRUCNA,
                VrstaSrednjeSkole.STRUCNA,
                VrstaSrednjeSkole.UMETNICKA,
                VrstaSrednjeSkole.ZANATSKA,
                VrstaSrednjeSkole.ZANATSKA,
                VrstaSrednjeSkole.GIMNAZIJA,
                VrstaSrednjeSkole.STRUCNA,
                VrstaSrednjeSkole.STRUCNA,
                VrstaSrednjeSkole.UMETNICKA,
                VrstaSrednjeSkole.ZANATSKA,
                VrstaSrednjeSkole.ZANATSKA,
                VrstaSrednjeSkole.GIMNAZIJA,
                VrstaSrednjeSkole.STRUCNA
        };

        if(srednjaSkolaRepository.count() == 0) {
            for(int i = 0; i < naziviSS.length; i++) {
                SrednjaSkola skola = new SrednjaSkola();
                skola.setSifra("SS" + (i + 1));
                skola.setNaziv(naziviSS[i]);
                skola.setVrstaSrednjeSkole(vrsteSS[i]);
                srednjaSkolaRepository.save(skola);
            }
        }

        // ----------- StudentPodaci 30 realnih studenata -----------
        if(studentPodaciRepository.count() == 0) {
            List<SrednjaSkola> srednjeSkole = (List<SrednjaSkola>) srednjaSkolaRepository.findAll();
            List<VisokoSkolskaUstanova> sveVSU = (List<VisokoSkolskaUstanova>) visokoSkolskaUstanovaRepository.findAll();

            List<String> imenaStudenata = Arrays.asList(
                    "Marko","Jovana","Petar","Ana","Stefan","Milica","Nikola","Ivana",
                    "Milan","Tamara","Luka","Sara","Nemanja","Marija","Aleksandar",
                    "Katarina","Filip","Teodora","Vladimir","Natasa","Maja","Dusan",
                    "Jelena","Bogdan","Kristina","Vuk","Sandra","Milos","Dragana","Ivan"
            );

            List<String> prezimenaStudenata = Arrays.asList(
                    "Jovanović","Petrović","Nikolić","Stojanović","Marković","Popović",
                    "Kovačević","Đorđević","Ristić","Lukić","Milenković","Vasić","Tomić",
                    "Pavlović","Milutinović","Janković","Stefanović","Radović","Knežević",
                    "Savić","Ostojić","Đukić","Lazarević","Vuković","Mitić","Stanojević",
                    "Antić","Mandić","Radosavljević","Živković"
            );

            Random random = new Random();

            for(int i = 0; i < 30; i++) {
                StudentPodaci student = new StudentPodaci();
                student.setIme(imenaStudenata.get(i));
                student.setPrezime(prezimenaStudenata.get(i));
                student.setSrednjeIme("Srednje" + i);
                student.setJmbg("01010012345" + String.format("%02d", i));
                student.setDatumRodjenja(LocalDate.of(2000 + random.nextInt(4), random.nextInt(12)+1, random.nextInt(28)+1));
                student.setMestoRodjenja("Grad" + (i+1));
                student.setMestoPrebivalista("Grad" + (i+1));
                student.setDrzavaRodjenja("Srbija");
                student.setDrzavljanstvo("SRB");
                student.setNacionalnost("Srbin");
                student.setPol(i % 2 == 0 ? 'M' : 'F');
                student.setAdresa("Ulica " + (i+1));
                student.setBrojTelefonaMobilni("06012345" + i);
                student.setBrojTelefonaFiksni("01112345" + i);
                student.setEmail(imenaStudenata.get(i).toLowerCase() + "." + prezimenaStudenata.get(i).toLowerCase() + "@example.com");
                student.setBrojLicneKarte("12345678" + i);
                student.setLicnuKartuIzdao("MUP Grad" + (i+1));
                student.setMestoStanovanja("Grad" + (i+1));
                student.setAdresaStanovanja("Ulica " + (i+1));

                student.setUspehIzSrednjeSkole(7 + random.nextDouble() * 3); // 7.0 - 10.0
                student.setPoeniSaPrijemnog(50 + random.nextDouble() * 50); // 50 - 100 poena

                // Random srednja škola
                student.setSrednjaSkola(srednjeSkole.get(random.nextInt(srednjeSkole.size())));

                // Random VSU sa 15% prelaznika
                if(random.nextDouble() < 0.15 && sveVSU.size() > 1) { // 15% prelaznika
                    VisokoSkolskaUstanova trenutnaVSU = sveVSU.get(random.nextInt(sveVSU.size()));
                    VisokoSkolskaUstanova prethodnaVSU;
                    do {
                        prethodnaVSU = sveVSU.get(random.nextInt(sveVSU.size()));
                    } while (prethodnaVSU.equals(trenutnaVSU));
                    student.setVisokoSkolskaUstanova(trenutnaVSU);
                } else {
                    student.setVisokoSkolskaUstanova(sveVSU.get(random.nextInt(sveVSU.size())));
                }

                studentPodaciRepository.save(student);
            }
        }

        // ----------- StudentIndeks i StudijskiTokAktivnosti (ISPRAVLJENO) -----------
        if(studentIndeksRepository.count() == 0) {
            List<StudentPodaci> studenti = studentPodaciRepository.findAll();
            List<StudijskiProgram> studProgrami = (List<StudijskiProgram>) studijskiProgramRepository.findAll();
            List<Predmet> predmeti = (List<Predmet>) predmetRepository.findAll();
            Random random = new Random();

            int globalBrojIndeksa = 1; // garantuje jedinstven broj indeksa

            for(StudentPodaci student : studenti) {
                // Odredimo koliko indeksa student ima (1 do 2 indeksa, neki prelaznici imaju 2)
                int brojIndeksa = random.nextDouble() < 0.15 ? 2 : 1; // 15% prelaznika

                for(int i = 0; i < brojIndeksa; i++) {
                    StudentIndeks indeks = new StudentIndeks();
                    indeks.setStudent(student);
                    StudijskiProgram sp = studProgrami.get(random.nextInt(studProgrami.size()));
                    indeks.setStudijskiProgram(sp);
                    indeks.setStudProgramOznaka(sp.getOznaka());
                    indeks.setGodina(2025); // tekuća godina upisa

                    // Jedinstveni broj indeksa
                    indeks.setBroj(globalBrojIndeksa++);
                    indeks.setNacinFinansiranja(random.nextBoolean() ? "Budžet" : "Samofinansiranje");
                    indeks.setAktivan(i == 0); // samo prvi indeks aktivan
                    indeks.setVaziOd(LocalDate.now().minusDays(random.nextInt(30)));
                    indeks.setOstvarenoEspb(0);

                    studentIndeksRepository.save(indeks);

                    // ---------- StudijskiTokAktivnosti ----------
                    // 1. Upis prve godine
                    StudijskiTokAktivnosti upis = new StudijskiTokAktivnosti();
                    upis.setTip(VrstaStudijskeAktivnosti.UPIS);
                    upis.setGodina(1);
                    upis.setDatum(LocalDate.now().minusDays(random.nextInt(10)));
                    upis.setNapomena("Automatski upis prve godine");
                    upis.setStudentIndeks(indeks);

                    // Dodeli 3 do 5 nasumičnih predmeta
                    Collections.shuffle(predmeti);
                    int brojPredmeta = Math.min(3 + random.nextInt(3), predmeti.size());
                    upis.setPredmet(new ArrayList<>(predmeti.subList(0, brojPredmeta)));

                    studijskiTokAktivnostiRepository.save(upis);

                    // 2. Eventualna obnova prve godine
                    if(random.nextDouble() < 0.2) { // 20% obnavlja
                        StudijskiTokAktivnosti obnova = new StudijskiTokAktivnosti();
                        obnova.setTip(VrstaStudijskeAktivnosti.OBNOVA);
                        obnova.setGodina(1);
                        obnova.setDatum(LocalDate.now().minusDays(random.nextInt(5)));
                        obnova.setNapomena("Automatska obnova godine");
                        obnova.setStudentIndeks(indeks);

                        Collections.shuffle(predmeti);
                        int brojPredmetaObnova = Math.min(2 + random.nextInt(2), predmeti.size());
                        obnova.setPredmet(new ArrayList<>(predmeti.subList(0, brojPredmetaObnova)));

                        studijskiTokAktivnostiRepository.save(obnova);
                    }

                    // 3. Upis druge godine
                    if(random.nextDouble() < 0.7) { // 70% prešli u 2. godinu
                        StudijskiTokAktivnosti drugaGodina = new StudijskiTokAktivnosti();
                        drugaGodina.setTip(VrstaStudijskeAktivnosti.UPIS);
                        drugaGodina.setGodina(2);
                        drugaGodina.setDatum(LocalDate.now().minusDays(random.nextInt(5)));
                        drugaGodina.setNapomena("Automatski upis druge godine");
                        drugaGodina.setStudentIndeks(indeks);

                        Collections.shuffle(predmeti);
                        int brojPredmetaDrugaGodina = Math.min(3 + random.nextInt(3), predmeti.size());
                        drugaGodina.setPredmet(new ArrayList<>(predmeti.subList(0, brojPredmetaDrugaGodina)));

                        studijskiTokAktivnostiRepository.save(drugaGodina);
                    }
                }
            }
        }
// ----------- SlusaPredmet -----------
        if(slusaPredmetRepository.count() == 0) {
            List<StudentIndeks> sviIndeksi = studentIndeksRepository.findAll();
            List<SkolskaGodina> sveSkolskeGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();
            Random random = new Random();

            for(StudentIndeks indeks : sviIndeksi) {
                StudijskiProgram sp = indeks.getStudijskiProgram();

                // Uzmi sve predmete za dati studijski program
                List<Predmet> predmetiZaSP = predmetRepository.findByStudProgram(sp);

                // Odredi aktivnu školsku godinu
                SkolskaGodina aktuelnaGodina = sveSkolskeGodine.stream()
                        .filter(SkolskaGodina::isAktivna)
                        .findFirst()
                        .orElse(sveSkolskeGodine.get(0));

                for(Predmet predmet : predmetiZaSP) {
                    boolean slusa = predmet.isObavezan() || random.nextDouble() < 0.5; // obavezni uvek, neobavezni 50% šanse
                    if(slusa) {
                        // Pronađi odgovarajući DrziPredmet za aktivnu godinu
                        List<DrziPredmet> dpZaPredmet = drziPredmetRepository.findByPredmetAndSkolskaGodina(predmet, aktuelnaGodina);
                        if(dpZaPredmet.isEmpty()) continue;

                        DrziPredmet dp = dpZaPredmet.get(random.nextInt(dpZaPredmet.size()));

                        SlusaPredmet spStudent = new SlusaPredmet();
                        spStudent.setStudentIndeks(indeks);
                        spStudent.setDrziPredmet(dp);
                        spStudent.setSkolskaGodina(dp.getSkolskaGodina());

                        slusaPredmetRepository.save(spStudent);
                    }
                }
            }
        }
        // ----------- PredispitneObaveze za svaki predmet -----------
        if(predispitneObavezeRepository.count() == 0) {
            List<Predmet> sviPredmeti = (List<Predmet>) predmetRepository.findAll();
            Random random = new Random();

            for(Predmet predmet : sviPredmeti) {
                // Random broj predispitnih obaveza između 1 i 3
                int brojObaveza = 1 + random.nextInt(3);

                for(int i = 0; i < brojObaveza; i++) {
                    PredispitneObaveze po = new PredispitneObaveze();
                    po.setPredmet(predmet);

                    // Postavimo maksimalan broj poena, npr. 10 do 30 poena
                    po.setMaksimalanBrojPoena(10 + random.nextInt(21));

                    // Nasumično dodeljujemo vrstu predispitne obaveze
                    VrstaPredispitneObaveze[] vrstePO = VrstaPredispitneObaveze.values();
                    po.setVrstaPredispitneObaveze(vrstePO[random.nextInt(vrstePO.length)]);

                    predispitneObavezeRepository.save(po);
                }
            }
        }
        // ----------- OstvareniPredispitni ----------
        if(ostvareniPredispitniRepository.count() == 0) {
            List<SlusaPredmet> sviSlusaPredmet = (List<SlusaPredmet>) slusaPredmetRepository.findAll();
            Random random = new Random();

            for(SlusaPredmet sp : sviSlusaPredmet) {
                Predmet predmet = sp.getDrziPredmet().getPredmet();
                List<PredispitneObaveze> obaveze = predispitneObavezeRepository.findByPredmet(predmet);
                if(obaveze.isEmpty()) continue;

                for(PredispitneObaveze po : obaveze) {
                    OstvareniPredispitni op = new OstvareniPredispitni();
                    op.setSlusaPredmet(sp);
                    op.setPredispitneObaveze(po);

                    // Nasumični poeni između 50% i 100% maksimalnih poena
                    double poeni = po.getMaksimalanBrojPoena() * (0.5 + random.nextDouble() * 0.5);
                    op.setOstvareniPoeni(Math.round(poeni * 100.0) / 100.0); // 2 decimale

                    ostvareniPredispitniRepository.save(op);
                }
            }
        }
        // ----------- PrijavaIspita i IzlazakNaIspit -----------
        if(prijavaIspitaRepository.count() == 0) {
            List<SlusaPredmet> sviSlusaPredmet = (List<SlusaPredmet>) slusaPredmetRepository.findAll();
            List<Ispit> sviIspiti = (List<Ispit>) ispitRepository.findAll();
            Random random = new Random();

            for(SlusaPredmet sp : sviSlusaPredmet) {

                // Ispiti koji pripadaju predmetu
                List<Ispit> ispitiZaPredmet = sviIspiti.stream()
                        .filter(ispit -> ispit.getDrziPredmet().getPredmet().getId()
                                .equals(sp.getDrziPredmet().getPredmet().getId()))
                        .collect(Collectors.toList());

                if(ispitiZaPredmet.isEmpty()) continue;

                // Odlučujemo da li student izlazi na ispit ili samo prijavljuje
                boolean izlazi = random.nextDouble() < 0.8; // 80% šanse da izađe

                Ispit izabraniIspit = ispitiZaPredmet.get(random.nextInt(ispitiZaPredmet.size()));

                // Kreiramo PrijavaIspita
                PrijavaIspita prijava = new PrijavaIspita();
                prijava.setSlusaPredmet(sp);
                prijava.setIspit(izabraniIspit);
                prijava.setDatumPrijave(LocalDate.now().minusDays(random.nextInt(10)));
                prijavaIspitaRepository.save(prijava);

                if(izlazi) {
                    IzlazakNaIspit izlazak = new IzlazakNaIspit();
                    izlazak.setPrijavaIspita(prijava);
                    // Broj poena sa ispita + predispitni poeni
                    double poeniPredispitni = ostvareniPredispitniRepository.findBySlusaPredmet(sp)
                            .stream()
                            .mapToDouble(OstvareniPredispitni::getOstvareniPoeni)
                            .sum();

// Generišemo poene sa ispita tako da ukupno bude realno između 51 i 100
                    double maxPoeniIspit = 100.0 - poeniPredispitni;
                    if(maxPoeniIspit < 0) maxPoeniIspit = 0;
                    double poeniIspit = poeniPredispitni >= 100 ? 0 : 1 + random.nextDouble() * (maxPoeniIspit - 1);

                    izlazak.setBrojPredispitnihPoena(poeniPredispitni);
                    izlazak.setBrojPoenaSaIspita(poeniIspit);

// Računanje ukupne ocene
                    double ukupniPoeni = poeniPredispitni + poeniIspit;
                    int ocena;
                    if(ukupniPoeni < 51) ocena = 5;
                    else if(ukupniPoeni <= 60) ocena = 6;
                    else if(ukupniPoeni <= 70) ocena = 7;
                    else if(ukupniPoeni <= 80) ocena = 8;
                    else if(ukupniPoeni <= 90) ocena = 9;
                    else ocena = 10;

                    izlazak.setOcena(ocena);
                    izlazak.setNapomena(random.nextDouble() < 0.1 ? "Ponistava" : "");
                    izlazak.setPonistava(false);
                    izlazakNaIspitRepository.save(izlazak);

// Povezujemo izlazak sa prijavom
                    prijava.setIzlazakNaIspita(izlazak);
                    prijavaIspitaRepository.save(prijava);

// Kreiramo PolozenPredmet samo ako je student položio
                    if(ocena >= 6) {
                        PolozenPredmet polozen = new PolozenPredmet();
                        polozen.setStudentIndeks(sp.getStudentIndeks());
                        polozen.setPredmet(sp.getDrziPredmet().getPredmet());
                        polozen.setIzlazakIspita(izlazak);

                        // --- Postavljanje datuma polaganja ---
                        LocalDate datumIspita = izabraniIspit.getDatumIspita();
                        // Dodajemo random varijaciju +/- 0-2 dana, ali ne izlazi iz logike
                        int randomOffset = random.nextInt(3); // 0,1,2
                        LocalDate datumPolaganja = datumIspita.plusDays(randomOffset);
                        polozen.setDatum(datumPolaganja);

                        polozen.setBrojPoena(ukupniPoeni);
                        polozen.setOcena(ocena);
                        polozen.setPriznato(false);

                        polozenPredmetRepository.save(polozen);
                    }

                }
            }
        }
    }
}
