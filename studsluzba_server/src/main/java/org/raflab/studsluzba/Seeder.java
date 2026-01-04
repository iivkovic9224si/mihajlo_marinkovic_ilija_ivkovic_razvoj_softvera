package org.raflab.studsluzba;

import enums.VrstaPredispitneObaveze;
import enums.VrstaSrednjeSkole;
import enums.VrstaStudijskeAktivnosti;
import enums.VrstaVisokoSkolskihUstanova;
import org.raflab.studsluzba.model.entity.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private UplataRepository uplataRepository;

    @Override
    public void run(String... args) throws Exception {

        List<StudijskiProgram> spList = new ArrayList<>();
        if (studijskiProgramRepository.count() == 0) {


            StudijskiProgram sp1 = new StudijskiProgram();
            sp1.setOznaka("RN");
            sp1.setNaziv("Racunarske Nauke");
            sp1.setGodinaAkreditacije(2016);
            sp1.setZvanje("Diplomirani inzenjer racunarskih nauka");
            sp1.setTrajanjeGodina(4);
            sp1.setTrajanjeSemestara(8);
            sp1.setVrstaStudija("OAS");
            sp1.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp1));

            StudijskiProgram sp2 = new StudijskiProgram();
            sp2.setOznaka("RI");
            sp2.setNaziv("Racunarsko Inzenjerstvo");
            sp2.setGodinaAkreditacije(2017);
            sp2.setZvanje("Diplomirani inzenjer racunarskog inzenjerstva");
            sp2.setTrajanjeGodina(4);
            sp2.setTrajanjeSemestara(8);
            sp2.setVrstaStudija("OAS");
            sp2.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp2));

            StudijskiProgram sp3 = new StudijskiProgram();
            sp3.setOznaka("SI");
            sp3.setNaziv("Softversko Inzenjerstvo");
            sp3.setGodinaAkreditacije(2022);
            sp3.setZvanje("Diplomirani inzenjer softverskog inzenjerstva");
            sp3.setTrajanjeGodina(4);
            sp3.setTrajanjeSemestara(8);
            sp3.setVrstaStudija("OAS");
            sp3.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp3));

            StudijskiProgram sp4 = new StudijskiProgram();
            sp4.setOznaka("IT");
            sp4.setNaziv("Informacione Tehnologije");
            sp4.setGodinaAkreditacije(2020);
            sp4.setZvanje("Strukovni dipl. inzenjer IT");
            sp4.setTrajanjeGodina(3);
            sp4.setTrajanjeSemestara(6);
            sp4.setVrstaStudija("OSS");
            sp4.setUkupnoEspb(180);
            spList.add(studijskiProgramRepository.save(sp4));
        }
        if(predmetRepository.count() == 0) {
        List<Predmet> predmetList = new ArrayList<>();

        Map<String, String[]> realniPredmeti = new HashMap<>();
        realniPredmeti.put("RN", new String[]{
                "Algoritmi i strukture podataka",
                "Dizajn softvera",
                "Inteligentni sistemi",
                "Napredna matematička analiza",
                "Operativni sistemi",
                "Računarske mreže",
                "Baze podataka",
                "Dizajn i analiza algoritama"
        });
        realniPredmeti.put("RI", new String[]{
                "Matematika 1",
                "Matematika 2",
                "Fizika",
                "Elektronika",
                "Operativni sistemi"
        });
        realniPredmeti.put("SI", new String[]{
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
                "Detaljno proučavanje struktura podataka i algoritama.",
                "Principi analize i dizajna softverskih sistema.",
                "Osnovni koncepti inteligentnih i autonomnih sistema.",
                "Napredne metode matematičke analize.",
                "Procesi, niti i memorijsko upravljanje u operativnim sistemima.",
                "Arhitektura i protokoli računarskih mreža.",
                "Koncepti baza podataka i SQL jezik.",
                "Napredni algoritmi i tehnike optimizacije."
        });
        opisiPredmeta.put("RI", new String[]{
                "Osnovni koncepti diferencijalnog i integralnog računa.",
                "Linearna algebra i diskretna matematika.",
                "Osnovi fizike primenjeni u tehnici.",
                "Digitalna elektronika i logički sklopovi.",
                "Osnovi operativnih sistema."
        });
        opisiPredmeta.put("SI", new String[]{
                "Objektno-orijentisani principi u programiranju.",
                "Razvoj modernih web aplikacija.",
                "Projektovanje i izrada softverskih projekata.",
                "Analiza zahteva i dizajn sistema.",
                "Tehnike testiranja i kontrola kvaliteta softvera."
        });
        opisiPredmeta.put("IT", new String[]{
                "Osnovni koncepti informacionih tehnologija.",
                "Osnovni mrežni protokoli i arhitekture.",
                "Uvod u Javu i OOP.",
                "SQL i rad sa relacionim bazama.",
                "Osnovni principi računarske bezbednosti."
        });

        int sifraCounter = 1;

        for (StudijskiProgram sp : spList) {
            String[] nazivi = realniPredmeti.get(sp.getOznaka());
            String[] opisi = opisiPredmeta.get(sp.getOznaka());

            for (int i = 0; i < nazivi.length; i++) {
                Predmet p = new Predmet();
                p.setSifra(String.format("PR%03d", sifraCounter++));
                p.setNaziv(nazivi[i]);
                p.setOpis(opisi[i]);
                p.setEspb(sp.getOznaka().equals("SI") ? 6 : 8);

                if (i == 1 || i == 2) {
                    p.setObavezan(false);
                } else {
                    p.setObavezan(true);
                }

                p.setStudProgram(sp);
                predmetList.add(predmetRepository.save(p));
            }
        }
        }

        if(visokoSkolskaUstanovaRepository.count()==0){
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
        }}

        if(nastavnikRepository.count() == 0) {
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

            nastavnikList.add(nastavnikRepository.save(n));

            int brojZvanja = (i % 3) + 1;
            for(int j = 0; j < brojZvanja; j++) {
                NastavnikZvanje nz = new NastavnikZvanje();
                nz.setNastavnik(n);
                nz.setDatumIzbora(LocalDate.of(2000 + i + j, (j % 12) + 1, ((i+j) % 28) + 1));
                nz.setNaucnaOblast(oblasti[(i+j) % oblasti.length]);
                nz.setUzaNaucnaOblast(oblasti[(i+j+1) % oblasti.length]);
                nz.setZvanje(zvanja[(i+j) % zvanja.length]);
                nz.setAktivno(j == 0);

                sveZvanjeList.add(nastavnikZvanjeRepository.save(nz));
            }
        }}



        LocalDate danas = LocalDate.now();
        int y = danas.getYear();
        LocalDate pocetakNoveSkolske = LocalDate.of(y, 10, 1); // 1. oktobar tekuće kalendarske
        int startGodine = danas.isBefore(pocetakNoveSkolske) ? (y - 1) : y;
        String nazivTekuceGodine = startGodine + "/" + (startGodine + 1);

        skolskaGodinaRepository.deactivateAll();

        for (int godina = 2016; godina <= y; godina++) {  // koristi y umesto trenutnaGodina
            String nazivGodine = godina + "/" + (godina + 1);

            Optional<SkolskaGodina> postojiGodina = skolskaGodinaRepository.findOneByGodina(nazivGodine);
            SkolskaGodina sg;

            if (postojiGodina.isPresent()) {
                sg = postojiGodina.get();
            } else {
                sg = new SkolskaGodina();
                sg.setGodina(nazivGodine);
            }

            sg.setAktivna(nazivGodine.equals(nazivTekuceGodine));
            skolskaGodinaRepository.save(sg);
        }



        if (drziPredmetRepository.count() == 0) {
            List<Nastavnik> nastavnici = nastavnikRepository.findAll();
            List<Predmet> predmeti = (List<Predmet>) predmetRepository.findAll();
            List<SkolskaGodina> skoleGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();

            SkolskaGodina aktuelnaGodina = null;
            for (SkolskaGodina sg : skoleGodine) {
                if (sg.isAktivna()) {
                    aktuelnaGodina = sg;
                    break;
                }
            }
            if (aktuelnaGodina == null && !skoleGodine.isEmpty()) {
                aktuelnaGodina = skoleGodine.get(0);
            }

            int nastavnikIndex = 0;
            for (Predmet p : predmeti) {
                Nastavnik n = nastavnici.get(nastavnikIndex);
                DrziPredmet dp = new DrziPredmet();
                dp.setPredmet(p);
                dp.setNastavnik(n);
                dp.setSkolskaGodina(aktuelnaGodina);
                drziPredmetRepository.save(dp);

                nastavnikIndex++;
                if (nastavnikIndex >= nastavnici.size()) {
                    nastavnikIndex = 0;
                }
            }

            nastavnikIndex = 1;
            for (Predmet p : predmeti) {
                Nastavnik n = nastavnici.get(nastavnikIndex);
                DrziPredmet dp = new DrziPredmet();
                dp.setPredmet(p);
                dp.setNastavnik(n);
                dp.setSkolskaGodina(aktuelnaGodina);
                drziPredmetRepository.save(dp);

                nastavnikIndex++;
                if (nastavnikIndex >= nastavnici.size()) {
                    nastavnikIndex = 0;
                }
            }
        }


        // ----------- Grupa -----------
        if (grupaRepository.count() == 0) {

            if (!spList.isEmpty()) {
                Grupa g1 = new Grupa();
                g1.setStudijskiProgram(spList.get(0));
                grupaRepository.save(g1);

                Grupa g2 = new Grupa();
                g2.setStudijskiProgram(spList.get(1));
                grupaRepository.save(g2);

                Grupa g3 = new Grupa();
                g3.setStudijskiProgram(spList.get(2));
                grupaRepository.save(g3);

                Grupa g4 = new Grupa();
                g4.setStudijskiProgram(spList.get(3));
                grupaRepository.save(g4);
            }
        }

        List<Grupa> sveGrupe = grupaRepository.findAll();

        for (Grupa g : sveGrupe) {
            List<Predmet> predmetiZaProgram = predmetRepository.findByStudProgram(g.getStudijskiProgram());

            g.setPredmeti(new ArrayList<>(predmetiZaProgram));

            grupaRepository.save(g);
        }

        if (ispitniRokRepository.count() == 0) {
            List<SkolskaGodina> skoleGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();

            for (SkolskaGodina sg : skoleGodine) {
                List<IspitniRok> rokovi = new ArrayList<>();

                IspitniRok jan = new IspitniRok();
                jan.setSkolskaGodina(sg);
                jan.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 1, 15));
                jan.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 1, 31));
                rokovi.add(jan);

                IspitniRok feb = new IspitniRok();
                feb.setSkolskaGodina(sg);
                feb.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 2, 15));
                feb.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 2, 28));
                rokovi.add(feb);

                IspitniRok jun = new IspitniRok();
                jun.setSkolskaGodina(sg);
                jun.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 6, 1));
                jun.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 6, 15));
                rokovi.add(jun);

                IspitniRok jul = new IspitniRok();
                jul.setSkolskaGodina(sg);
                jul.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 7, 1));
                jul.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 7, 15));
                rokovi.add(jul);

                IspitniRok avg = new IspitniRok();
                avg.setSkolskaGodina(sg);
                avg.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 8, 1));
                avg.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 8, 15));
                rokovi.add(avg);

                IspitniRok sep = new IspitniRok();
                sep.setSkolskaGodina(sg);
                sep.setPocetak(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 9, 1));
                sep.setKraj(LocalDate.of(Integer.parseInt(sg.getGodina().substring(0, 4)), 9, 15));
                rokovi.add(sep);

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

                // 3. Uzmi sve ispitne rokove iz aktivne godine
                List<IspitniRok> sviRokovi = (List<IspitniRok>) ispitniRokRepository.findAll();
                List<IspitniRok> rokoviAktuelnaGodina = new ArrayList<>();
                for (IspitniRok rok : sviRokovi) {
                    if (rok.getSkolskaGodina().getId().equals(aktuelnaGodina.getId())) {
                        rokoviAktuelnaGodina.add(rok);
                    }
                }

                // 4. Fiksni datumi za 15 ispita (deterministički, svaki put isti)
                LocalDate[] datumiIspita = new LocalDate[] {
                        LocalDate.of(2025, 1, 16), LocalDate.of(2025, 1, 20),
                        LocalDate.of(2025, 2, 16), LocalDate.of(2025, 2, 20),
                        LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 5),
                        LocalDate.of(2025, 6, 10), LocalDate.of(2025, 7, 2),
                        LocalDate.of(2025, 7, 5), LocalDate.of(2025, 7, 10),
                        LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 5),
                        LocalDate.of(2025, 8, 10), LocalDate.of(2025, 9, 2),
                        LocalDate.of(2025, 9, 5)
                };

                // 5. Kreiranje ispita i povezivanje sa ispitnim rokom
                for (int i = 0; i < 15; i++) {
                    DrziPredmet dpZaIspit = dpAktuelnaGodina.get(i % dpAktuelnaGodina.size());

                    Ispit ispit = new Ispit();
                    ispit.setDrziPredmet(dpZaIspit);
                    ispit.setDatumIspita(datumiIspita[i]);
                    ispit.setVremePocetkaIspita(LocalTime.of(9 + (i % 5), 0)); // ciklično vreme
                    ispit.setZakljucen(false);

                    // --- povezivanje sa odgovarajućim ispitnim rokom ---
                    for (IspitniRok rok : rokoviAktuelnaGodina) {
                        if (!ispit.getDatumIspita().isBefore(rok.getPocetak()) &&
                                !ispit.getDatumIspita().isAfter(rok.getKraj())) {

                            ispit.setIspitniRok(rok); // VEZA sa instancom iz baze
                            break; // nađi prvi odgovarajući rok i izlazi
                        }
                    }

                    // --- sačuvaj ispit nakon što je povezan sa rokom ---
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

            for(int i = 0; i < 30; i++) {
                StudentPodaci student = new StudentPodaci();
                student.setIme(imenaStudenata.get(i));
                student.setPrezime(prezimenaStudenata.get(i));
                student.setSrednjeIme("Srednje" + i);
                student.setJmbg("01010012345" + String.format("%02d", i));
                student.setDatumRodjenja(LocalDate.of(2000 + (i % 4), (i % 12) + 1, (i % 28) + 1));
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
                student.setUspehIzSrednjeSkole(7.0 + (i % 4) * 0.5);
                student.setPoeniSaPrijemnog(50 + (i % 51));
                student.setSrednjaSkola(srednjeSkole.get(i % srednjeSkole.size()));

                if(i == 3 || i == 7 || i == 15 || i == 23) {
                    VisokoSkolskaUstanova trenutnaVSU = sveVSU.get(i % sveVSU.size());
                    VisokoSkolskaUstanova prethodnaVSU = sveVSU.get((i+1) % sveVSU.size());
                    student.setVisokoSkolskaUstanova(trenutnaVSU);
                    student.setVisokoSkolskaUstanova(prethodnaVSU);
                } else {
                    student.setVisokoSkolskaUstanova(sveVSU.get(i % sveVSU.size()));
                }

                studentPodaciRepository.save(student);


            }
        }




        // ----------- StudentIndeks i StudijskiTokAktivnosti (ISPRAVLJENO) -----------
            List<StudentPodaci> studenti = studentPodaciRepository.findAll();
            List<StudijskiProgram> studProgrami = (List<StudijskiProgram>) studijskiProgramRepository.findAll();
            List<Predmet> predmeti = (List<Predmet>) predmetRepository.findAll();
            int globalBrojIndeksa = 1;
            Map<String, Set<Integer>> zauzetiBrojevi = new HashMap<>();

            for(int i = 0; i < studenti.size(); i++) {
                StudentPodaci student = studenti.get(i);
                int brojIndeksa = (i % 5 == 0) ? 2 : 1;

                for(int j = 0; j < brojIndeksa; j++) {
                    StudentIndeks indeks = new StudentIndeks();
                    indeks.setStudent(student);
                    StudijskiProgram sp = studProgrami.get(i % studProgrami.size());
                    indeks.setStudijskiProgram(sp);
                    indeks.setStudProgramOznaka(sp.getOznaka());
                    int godina = 2025 + j;
                    indeks.setGodina(godina);
                    String key = sp.getOznaka() + "-" + godina;
                    zauzetiBrojevi.putIfAbsent(key, new HashSet<>());

                    int broj;
                    if(i == 0 && j == 0) {
                        broj = 100;
                    } else if(i == 1 && j == 0) {
                        broj = 100;
                    } else if(i == 2 && j == 0) {
                        broj = 100;
                    } else {
                        broj = globalBrojIndeksa++;
                    }

                    while(zauzetiBrojevi.get(key).contains(broj)) {
                        broj++;
                    }
                    zauzetiBrojevi.get(key).add(broj);
                    indeks.setBroj(broj);
                    indeks.setNacinFinansiranja("Budžet");
                    indeks.setAktivan(j == 0);
                    indeks.setVaziOd(LocalDate.of(2025, 1, 1));
                    indeks.setOstvarenoEspb(0);
                    studentIndeksRepository.save(indeks);
                    StudijskiTokAktivnosti upis = new StudijskiTokAktivnosti();
                    upis.setTip(VrstaStudijskeAktivnosti.UPIS);
                    upis.setGodina(1);
                    upis.setDatum(LocalDate.of(2025, 9, 1));
                    upis.setNapomena("Upis prve godine");
                    upis.setStudentIndeks(indeks);
                    upis.setPredmet(Arrays.asList(predmeti.get(i % predmeti.size()),
                            predmeti.get((i + 1) % predmeti.size()),
                            predmeti.get((i + 2) % predmeti.size())));
                    studijskiTokAktivnostiRepository.save(upis);
                }
            }
        if (studijskiTokAktivnostiRepository.count() > 0) {
            List<StudentIndeks> sviIndeksi = studentIndeksRepository.findAll();
            List<Predmet> sviPredmeti = (List<Predmet>) predmetRepository.findAll();

            for (int i = 0; i < sviIndeksi.size(); i++) {
                StudentIndeks indeks = sviIndeksi.get(i);

                // Kreiraj novu obnavljanje godine
                StudijskiTokAktivnosti obnova = new StudijskiTokAktivnosti();
                obnova.setTip(VrstaStudijskeAktivnosti.OBNOVA);
                obnova.setGodina(2); // recimo obnova druge godine
                obnova.setDatum(LocalDate.of(2025, 10, 1));
                obnova.setNapomena("Obnova godine - test podaci");
                obnova.setStudentIndeks(indeks);

                // Izbor predmeta: nepoloženi iz prethodne godine + neki iz naredne
                List<Predmet> predmetiZaObnovu = new ArrayList<>();
                double espbUkupno = 0;

                // Dodaj nepoložene predmete iz prethodne godine (primer: uzimamo prvih 3 predmeta)
                for (int j = 0; j < 3 && espbUkupno < 60; j++) {
                    Predmet p = sviPredmeti.get((i + j) % sviPredmeti.size());
                    if (espbUkupno + p.getEspb() <= 60) {
                        predmetiZaObnovu.add(p);
                        espbUkupno += p.getEspb();
                    }
                }

                // Dodaj predmete iz naredne godine dok ne dostignemo ESPB limit
                for (int j = 3; j < 6 && espbUkupno < 60; j++) {
                    Predmet p = sviPredmeti.get((i + j) % sviPredmeti.size());
                    if (espbUkupno + p.getEspb() <= 60) {
                        predmetiZaObnovu.add(p);
                        espbUkupno += p.getEspb();
                    }
                }

                obnova.setPredmet(predmetiZaObnovu);
                studijskiTokAktivnostiRepository.save(obnova);
            }
        }

// ----------- SlusaPredmet -----------
        if(slusaPredmetRepository.count() == 0) {
            List<StudentIndeks> sviIndeksi = studentIndeksRepository.findAll();
            List<SkolskaGodina> sveSkolskeGodine = (List<SkolskaGodina>) skolskaGodinaRepository.findAll();

            for(int i = 0; i < sviIndeksi.size(); i++) {
                StudentIndeks indeks = sviIndeksi.get(i);
                StudijskiProgram sp = indeks.getStudijskiProgram();
                List<Predmet> predmetiZaSP = predmetRepository.findByStudProgram(sp);

                SkolskaGodina aktuelnaGodina = sveSkolskeGodine.stream()
                        .filter(SkolskaGodina::isAktivna)
                        .findFirst()
                        .orElse(sveSkolskeGodine.get(0));

                for(int j = 0; j < predmetiZaSP.size(); j++) {
                    Predmet predmet = predmetiZaSP.get(j);
                    boolean slusa = predmet.isObavezan() || (i + j) % 2 == 0;

                    if(slusa) {
                        List<DrziPredmet> dpZaPredmet = drziPredmetRepository.findByPredmetAndSkolskaGodina(predmet, aktuelnaGodina);
                        if(dpZaPredmet.isEmpty()) continue;

                        DrziPredmet dp = dpZaPredmet.get((i + j) % dpZaPredmet.size());

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
            VrstaPredispitneObaveze[] vrstePO = VrstaPredispitneObaveze.values();

            for(int i = 0; i < sviPredmeti.size(); i++) {
                Predmet predmet = sviPredmeti.get(i);
                int brojObaveza = (i % 3) + 1;

                for(int j = 0; j < brojObaveza; j++) {
                    PredispitneObaveze po = new PredispitneObaveze();
                    po.setPredmet(predmet);
                    po.setMaksimalanBrojPoena(10 + j * 10);
                    po.setVrstaPredispitneObaveze(vrstePO[j % vrstePO.length]);
                    predispitneObavezeRepository.save(po);
                }
            }
        }

        // ----------- OstvareniPredispitni ----------
        if(ostvareniPredispitniRepository.count() == 0) {
            List<SlusaPredmet> sviSlusaPredmet = (List<SlusaPredmet>) slusaPredmetRepository.findAll();
            List<PredispitneObaveze> sveObaveze = (List<PredispitneObaveze>) predispitneObavezeRepository.findAll();

            for(int i = 0; i < sviSlusaPredmet.size(); i++) {
                SlusaPredmet sp = sviSlusaPredmet.get(i);
                Predmet predmet = sp.getDrziPredmet().getPredmet();
                List<PredispitneObaveze> obaveze = sveObaveze.stream()
                        .filter(po -> po.getPredmet().getId().equals(predmet.getId()))
                        .collect(Collectors.toList());
                for(int j = 0; j < obaveze.size(); j++) {
                    PredispitneObaveze po = obaveze.get(j);
                    OstvareniPredispitni op = new OstvareniPredispitni();
                    op.setSlusaPredmet(sp);
                    op.setPredispitneObaveze(po);
                    op.setOstvareniPoeni(Math.round((po.getMaksimalanBrojPoena() * (0.75 + j*0.05)) * 100.0) / 100.0);
                    ostvareniPredispitniRepository.save(op);
                }
            }
        }

        if(prijavaIspitaRepository.count() == 0) {
            List<SlusaPredmet> sviSlusaPredmet = (List<SlusaPredmet>) slusaPredmetRepository.findAll();
            List<Ispit> sviIspiti = (List<Ispit>) ispitRepository.findAll();

            for(int i = 0; i < sviSlusaPredmet.size(); i++) {
                SlusaPredmet sp = sviSlusaPredmet.get(i);
                List<Ispit> ispitiZaPredmet = sviIspiti.stream()
                        .filter(ispit -> ispit.getDrziPredmet().getPredmet().getId()
                                .equals(sp.getDrziPredmet().getPredmet().getId()))
                        .collect(Collectors.toList());
                if(ispitiZaPredmet.isEmpty()) continue;

                boolean izlazi = (i % 5 != 0);
                Ispit izabraniIspit = ispitiZaPredmet.get(i % ispitiZaPredmet.size());

                PrijavaIspita prijava = new PrijavaIspita();
                prijava.setSlusaPredmet(sp);
                prijava.setIspit(izabraniIspit);
                prijava.setDatumPrijave(LocalDate.now().minusDays(i % 10));
                prijavaIspitaRepository.save(prijava);

                if(izlazi) {
                    IzlazakNaIspit izlazak = new IzlazakNaIspit();
                    izlazak.setPrijavaIspita(prijava);
                    double poeniPredispitni = ostvareniPredispitniRepository.findBySlusaPredmet(sp)
                            .stream()
                            .mapToDouble(OstvareniPredispitni::getOstvareniPoeni)
                            .sum();
                    double maxPoeniIspit = Math.max(0, 100.0 - poeniPredispitni);
                    double poeniIspit = Math.min(maxPoeniIspit, 20 + (i % 10) * 5);
                    izlazak.setBrojPredispitnihPoena(poeniPredispitni);
                    izlazak.setBrojPoenaSaIspita(poeniIspit);
                    double ukupniPoeni = poeniPredispitni + poeniIspit;
                    int ocena;
                    if(ukupniPoeni < 51) ocena = 5;
                    else if(ukupniPoeni <= 60) ocena = 6;
                    else if(ukupniPoeni <= 70) ocena = 7;
                    else if(ukupniPoeni <= 80) ocena = 8;
                    else if(ukupniPoeni <= 90) ocena = 9;
                    else ocena = 10;
                    izlazak.setNapomena("");
                    izlazak.setPonistava(false);
                    izlazakNaIspitRepository.save(izlazak);
                    prijava.setIzlazakNaIspita(izlazak);
                    prijavaIspitaRepository.save(prijava);

                    if(ocena >= 6) {
                        PolozenPredmet polozen = new PolozenPredmet();
                        polozen.setStudentIndeks(sp.getStudentIndeks());
                        polozen.setPredmet(sp.getDrziPredmet().getPredmet());
                        polozen.setIzlazakIspita(izlazak);
                        polozen.setDatum(izabraniIspit.getDatumIspita().plusDays(i % 3));
                        polozen.setBrojPoena(ukupniPoeni);
                        polozen.setOcena(ocena);
                        polozen.setPriznato(false);
                        polozenPredmetRepository.save(polozen);
                    }


                }

            }
            Predmet predmet = predmetRepository.findBySifra("PR001").orElseThrow();

// Lista ocena koju želimo da podelimo po ispitima
            int[] ocene = {10, 9, 8, 7, 6};

// --- Izlasci u 2023 ---
            for (int i = 0; i < 5; i++) {
                IzlazakNaIspit izlazak = new IzlazakNaIspit();
                izlazakNaIspitRepository.save(izlazak);

                // Kreiranje PolozenPredmet sa ocenom
                PolozenPredmet polozen = new PolozenPredmet();
                polozen.setPredmet(predmet);
                polozen.setIzlazakIspita(izlazak);
                polozen.setOcena(ocene[i]); // ocena od 10 do 6
                polozen.setDatum(LocalDate.of(2023,4,1));
                polozenPredmetRepository.save(polozen);
            }

// --- Izlasci u 2024 ---
            for (int i = 0; i < 3; i++) {
                IzlazakNaIspit izlazak = new IzlazakNaIspit();
                izlazakNaIspitRepository.save(izlazak);

                // Kreiranje PolozenPredmet sa ocenom (ponovo 10,9,8 za raznovrsnost)
                PolozenPredmet polozen = new PolozenPredmet();
                polozen.setPredmet(predmet);
                polozen.setIzlazakIspita(izlazak);
                polozen.setOcena(10 - i); // 10, 9, 8
                polozen.setDatum(LocalDate.of(2024,10,1));
                polozenPredmetRepository.save(polozen);
            }

        }

        // ----------- Uplate za prvih 10 studenata -----------
        // ----------- Uplate -----------
        if(uplataRepository.count() == 0) {
            List<StudentPodaci> sviStudenti = studentPodaciRepository.findAll();

            // Fiksni iznosi i datumi (svi < 300000)
            double[] iznosi = {50000, 75000, 100000, 125000, 150000, 175000, 200000, 225000, 250000, 275000};
            LocalDate[] datumi = {
                    LocalDate.of(2025, 1, 15),
                    LocalDate.of(2025, 2, 15),
                    LocalDate.of(2025, 3, 15),
                    LocalDate.of(2025, 4, 15),
                    LocalDate.of(2025, 5, 15),
                    LocalDate.of(2025, 6, 15),
                    LocalDate.of(2025, 7, 15),
                    LocalDate.of(2025, 8, 15),
                    LocalDate.of(2025, 9, 15),
                    LocalDate.of(2025, 10, 15)
            };

            // Dodajemo uplate za prvih 10 studenata
            for (int i = 0; i < 10 && i < sviStudenti.size(); i++) {
                Uplata uplata = new Uplata();
                uplata.setStudentPodaci(sviStudenti.get(i));
                uplata.setIznosUDinarima(iznosi[i]);
                uplata.setDatumUplate(datumi[i]);
                uplataRepository.save(uplata);
            }
        }
        // ✅ NAJBRŽE REŠENJE - Koristi postojećeg studenta (prvi iz baze)
        if (prijavaIspitaRepository.count() > 0) {
            LocalDate sada = LocalDate.now();
            Pageable pageable = PageRequest.of(0, 1);
            Page<StudentPodaci> studentPage = studentPodaciRepository.findAll(pageable);
            if (studentPage.getContent().isEmpty()) return;
            StudentPodaci student = studentPage.getContent().get(0); // bilo koji student

            // 1. NAPRAVI INDEKS 2025/100/RN za njega
            StudentIndeks studentIndeks = studentIndeksRepository.findStudentIndeks("RN", 2025, 100);
            if (studentIndeks == null) {
                StudijskiProgram rnProgram = studijskiProgramRepository.findByOznakaSingle("RN");
                if (rnProgram == null) return;
                studentIndeks = new StudentIndeks();
                studentIndeks.setStudent(student);
                studentIndeks.setStudijskiProgram(rnProgram);
                studentIndeks.setStudProgramOznaka("RN");
                studentIndeks.setGodina(2025);
                studentIndeks.setBroj(100);
                studentIndeks.setNacinFinansiranja("Budžet");
                studentIndeks.setAktivan(true);
                studentIndeks.setVaziOd(LocalDate.of(2025, 9, 1));
                studentIndeks.setOstvarenoEspb(0);
                studentIndeks = studentIndeksRepository.save(studentIndeks);
            }

            Predmet pr001 = predmetRepository.findBySifra("PR001").orElse(null);
            if (pr001 == null) return;

            Nastavnik markoNastavnik = nastavnikRepository.findByEmail("marko.jovanović@example.com").orElse(null);
            if (markoNastavnik == null) return;

            SkolskaGodina aktivnaGodina = skolskaGodinaRepository.findByGodina("2025/2026");
            if (aktivnaGodina == null) return;

            // 2. DrziPredmet PR001 + nastavnik + 2025/2026
            DrziPredmet dp = drziPredmetRepository
                    .findBySkolskaGodinaGodinaAndNastavnikEmailAndPredmetSifra(
                            "2025/2026", "marko.jovanović@example.com", "PR001")
                    .orElseGet(() -> {
                        DrziPredmet novi = new DrziPredmet();
                        novi.setPredmet(pr001);
                        novi.setNastavnik(markoNastavnik);
                        novi.setSkolskaGodina(aktivnaGodina);
                        return drziPredmetRepository.save(novi);
                    });

            // 3. Ispitni rok Januar 2026
            IspitniRok januarRok = null;
            for (IspitniRok r : ispitniRokRepository.findAll()) {
                if (r.getSkolskaGodina().getId().equals(aktivnaGodina.getId())
                        && r.getPocetak().equals(LocalDate.of(sada.getYear(), sada.getMonth(), 1))
                        && r.getKraj().equals(LocalDate.of(sada.getYear(), sada.getMonth(), 31))) {
                    januarRok = r;
                    break;
                }
            }
            if (januarRok == null) {
                januarRok = new IspitniRok();
                januarRok.setSkolskaGodina(aktivnaGodina);
                januarRok.setPocetak(LocalDate.of(sada.getYear(), sada.getMonth(), 1));
                januarRok.setKraj(LocalDate.of(sada.getYear(), sada.getMonth(), 31));
                januarRok = ispitniRokRepository.save(januarRok);
            }

            // 4. Ispit 3.1.2026 12:00
            Ispit ispit = null;
            for (Ispit i : ispitRepository.findAll()) {
                if (i.getDrziPredmet().getId().equals(dp.getId())
                        && i.getDatumIspita().equals(LocalDate.of(sada.getYear(), sada.getMonth(), sada.getDayOfMonth()+1))
                        && i.getVremePocetkaIspita().equals(LocalTime.of(12, 0))) {
                    ispit = i;
                    break;
                }
            }
            if (ispit == null) {
                ispit = new Ispit();
                ispit.setDrziPredmet(dp);
                ispit.setIspitniRok(januarRok);
                ispit.setDatumIspita(LocalDate.of(sada.getYear(), sada.getMonth(), sada.getDayOfMonth()+1));
                ispit.setVremePocetkaIspita(LocalTime.of(12, 0));
                ispit.setZakljucen(true);
                ispit = ispitRepository.save(ispit);
            } else if (!ispit.isZakljucen()) {
                ispit.setZakljucen(true);
                ispit = ispitRepository.save(ispit);
            }

            // 5. SlusaPredmet
            Optional<SlusaPredmet> postojeceSlusa = slusaPredmetRepository
                    .findByStudentIndeksAndDrziPredmet(1l, 1l).stream().findFirst();
            StudentIndeks finalStudentIndeks = studentIndeks;
            SlusaPredmet slusaPredmet = postojeceSlusa.orElseGet(() -> {
                SlusaPredmet novi = new SlusaPredmet();
                novi.setStudentIndeks(finalStudentIndeks);
                novi.setDrziPredmet(dp);
                novi.setSkolskaGodina(aktivnaGodina);
                return slusaPredmetRepository.save(novi);
            });

            // 6. PRIJAVA - FORSIRA
            List<PrijavaIspita> postojecePrijave = prijavaIspitaRepository.findBySlusaPredmetAndIspit(slusaPredmet, ispit);
            PrijavaIspita prijava;
            if (postojecePrijave.isEmpty()) {
                prijava = new PrijavaIspita();
                prijava.setSlusaPredmet(slusaPredmet);
                prijava.setIspit(ispit);
                prijava.setDatumPrijave(LocalDate.of(2025, 12, 20));
                prijava = prijavaIspitaRepository.save(prijava);
            } else {
                prijava = postojecePrijave.get(0);
                prijava.setDatumPrijave(LocalDate.of(2025, 12, 20));
                prijava = prijavaIspitaRepository.save(prijava);
            }

//            // 7. IZLAZAK - FORSIRA
//            List<IzlazakNaIspit> postojeceIzlazak = izlazakNaIspitRepository.findByPrijavaIspitaId(prijava.getId());
//            if (postojeceIzlazak.isEmpty()) {
//                IzlazakNaIspit izlazak = new IzlazakNaIspit();
//                izlazak.setPrijavaIspita(prijava);
//                izlazak.setBrojPredispitnihPoena(25.0);
//                izlazak.setBrojPoenaSaIspita(50.0);
//                izlazak.setNapomena("Test položen za frontend");
//                izlazak.setPonistava(false);
//                izlazakNaIspitRepository.save(izlazak);
//                prijava.setIzlazakNaIspita(izlazak);
//                prijavaIspitaRepository.save(prijava);
//            }
        }


    }
}
