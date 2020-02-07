package Data_Manipulation_Module;

import Database_Connectivity_Module.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class Data_Manipulation_Controller {
    public MetricsInterface getmInf() {
        return mInf;
    }

    private MetricsInterface mInf;
    private Plausibility plausi;
    private QueryAssembler queryA;
    private Data_Manipulation_Interface dataInferface;
    private Database_Connectivity_Interface connectivity_interface;
    //private List<Schichtarbeitstag> alleTage;
    private Schichtarbeitstag[] alleTage;
    private int[][] monatsArbeitstage;
    List<Kalenderwoche> kwList;
    private Schichtarbeitstag[] candidates;
    private List<Kalenderwoche> kwListCopy;
    private Fahrplan fahrplan;
    private int[] abfahrtstage;

    public Data_Manipulation_Controller(Database_Connectivity_Interface conI) {
        mInf = new MetricsInterface();
        plausi = new Plausibility();
        queryA = new QueryAssembler();
        queryA.enlistController(this);
        dataInferface = new Data_Manipulation_Interface();
        dataInferface.enlistController(this);
        this.connectivity_interface = conI;
        fahrplanGenerieren();
        prepareListofAllDays();
        monatsArbeitstage = prepareMonatsarbeitstage();
        kwList = null;
    }

    public Data_Manipulation_Controller(String[] args) {
        mInf = new MetricsInterface();
        plausi = new Plausibility();
        queryA = new QueryAssembler();
        queryA.enlistController(this);
        dataInferface = new Data_Manipulation_Interface();
        dataInferface.enlistController(this);
        this.connectivity_interface = null;
        alleTage = new Schichtarbeitstag[520];
        monatsArbeitstage = new int[12][15];
        kwList = null;
    }

    public Data_Manipulation_Controller() {
        mInf = null;
        plausi = null;
        dataInferface = null;
        connectivity_interface = null;
        alleTage = null;
        monatsArbeitstage = null;
        kwList = null;
    }

    private void fahrplanGenerieren() {
       // abfahrtstage = queryA.getFahrplanTage();
    }

    private int[][] prepareMonatsarbeitstage() {
        int[][] tagInfos = new int[12][2];
        for (int i = 0; i < 12; i++) {
            Integer[] tage = queryA.getVorgabeMonatTage(i + 1);
            tagInfos[i][0] = tage[0];
            tagInfos[i][1] = tage[1];
        }
        return tagInfos;
    }

    /**
     * checks if the input day is in the past already
     *
     * @param tagID day to be checked
     * @return true, if the day is at least yesterday
     */
    public boolean isDayinPast(int tagID) {
        return tagID < this.getToday(0);
    }


    /**
     * The amount of a part that is scheduled to be delivered by the start of the week
     *
     * @param tagID  production day
     * @param partID part id
     * @return the amount
     */
    public int getDeliveryAmount(int tagID, int partID) {
        return 0;
    }


    public ResultSet getQueryResult(String sql) {
        return connectivity_interface.getQueryresult(sql);
    }

    public boolean isHoliday(int tagID) {
        return queryA.isHoliday(tagID, "Nordrhein-Westfalen");
    }

    public QueryAssembler getQueryA() {
        return queryA;
    }


    private boolean isPossibleNoOrder(int fahrradID, int anzahl, int lieferdatumID) {

        boolean returnValue = true;

        for (int i = 1; i <= 3; i++) {
            // returnValue = returnValue && hasEnoughStock(komponentenIds[i], anzahl, getPreviousWorkingDay(lieferdatumID));
        }
        return returnValue;
    }


    /**
     * berechnet aus den monatlichen Zielvorgaben die vorgaben für die jeweiligen arbeitstage
     */
    public void zielVorgabenWorkflow() {
        System.out.println("    Zielvorgaben werden berechnet...");
        int vorgabeMonat;
        for (int monat = 1; monat <= 12; monat++) {
            Schichtarbeitstag[] arbeitstageDiesenMonat = getArbeitstagArray(monat);
            for (Schichtarbeitstag s : arbeitstageDiesenMonat) {
                s.setMonats_ID(monat);
            }
            int arbeitstagCounter = countArbeitstage(arbeitstageDiesenMonat);
            for (int fahrrad = 1; fahrrad <= 8; fahrrad++) {
                vorgabeMonat = queryA.getZielvorgabe(monat, fahrrad);
                fillProdVolume(arbeitstageDiesenMonat, vorgabeMonat, arbeitstagCounter, fahrrad);//ok
            }
            queryA.produktionsvolumenAnlegen(arbeitstageDiesenMonat);
        }
        this.kwList = Kalenderwoche.getKWListFromTage(alleTage);
        System.out.println("    Schichten werden berechnet...");
        schichtenworkflow();
        //schichtenStabilisieren();

        System.out.println("    Lagerbestände werden berechnet...");
        System.out.println("    Lieferungen werden berechnet...");
        for (Kalenderwoche kw : kwList) {
            lagerbestandworkflow(kw);
            lieferungworkflow(kw);
            bestellungworkflow(kw.getVorwoche());
            lagerbestandworkflow(kw);
        }
        for (int i = 0; i < alleTage.length; i++) {
            alleTage[i].checkMyself();
        }
        System.out.println("    Berechnete Werte werden festgeschrieben...");
        //lagerbestandfestschreiben();
        for (int x = 0; x < alleTage.length; x++) {
            for (Lieferung l : alleTage[x].getLieferungen()) {
                // queryA.lieferungfestschrieben(l);
            }
            for (Bestellung b : alleTage[x].getBestellungen()) {
                // queryA.bestellungenfestschreiben(b);
            }
        }
        candidatesherstellen();
    }

    private void candidatesherstellen() {
        candidates = new Schichtarbeitstag[alleTage.length];
        Schichtarbeitstag vortag = null;
        for (int original = 0; original < alleTage.length; original++) {
            Schichtarbeitstag copy = alleTage[original].getCopy();
            copy.setVortag(vortag);
            candidates[original] = copy;
            vortag = copy;
        }
        kwListCopy = Kalenderwoche.getKWListFromTage(candidates);
    }

    private void schichtenStabilisieren() {
        for (Kalenderwoche kw : kwList) {
            Schichtarbeitstag d = kw.getLast();
            int max = 0;
            while (kw.next()) {
                d = kw.get();
                if (d.getSchicht_ID() > max) {
                    max = d.getSchicht_ID();
                }
            }
            kw.reset();
            while (kw.next()) {
                d = kw.get();
                if (!d.isHoldayIn(Land.Nordrhein_Westfalen)) {
                    d.setSchicht_ID(max);
                    d.limitAnpassen(520 * max);
                }
            }
        }
    }

    private void bestellungworkflow(Kalenderwoche kw) {
        if (kw != null) {
            Schichtarbeitstag tag = kw.getLast();
            LinkedList<Schichtarbeitstag> zuBerucksichtigen = new LinkedList<>();
            while (tag.isHoldayIn(Land.Nordrhein_Westfalen)) {
                zuBerucksichtigen.add(tag);
                tag = tag.getVortag();
            }
            if (tag.getsum() > 0) {
                for (int i = 1; i <= 8; i++) {
                    tag.addBestellung(new Bestellung(tag.getArbeitstag_ID(), tag.getArbeitstag_ID(), i, tag.getLagerbestand(i), this.getToday(), tag.getDatum()));
                }
            }
            while (zuBerucksichtigen.size() > 0) {
                Schichtarbeitstag berucksichtigt = zuBerucksichtigen.removeLast();
                berucksichtigt.lagerbestandBerechnen(berucksichtigt.getVortag());
            }
        }
    }

    private void lagerbestandfestschreiben() {
        for (int i = 0; i < alleTage.length; i++) {
            queryA.setLagerbestand(alleTage[i]);
        }

    }

    /**
     * zieht aus der Datenbank alle Tage und setzt sie in die benötigte Datenstruktur
     *
     * @return eine Liste von Schichtarbeitstagen
     */
    private void prepareListofAllDays() {
        alleTage = new Schichtarbeitstag[520];
        Schichtarbeitstag tag = null;
        Schichtarbeitstag vortag = new Schichtarbeitstag(0, 0, new int[8], 0, 0, new HashSet<Land>(), "01.01.1901");
        vortag.setVortag(null);
        alleTage[0] = vortag;
        for (int tagId = 1; tagId < alleTage.length; tagId++) {
            tag = queryA.getSchicht(tagId);
            tag.setVortag(vortag);
            vortag = tag;
            alleTage[tagId] = tag;
        }
    }

    private int countArbeitstage(Schichtarbeitstag[] arbeitstageDiesenMonat) {
        int arbeitstag_counter = 0;
        for (int i = 0; i < arbeitstageDiesenMonat.length; i++) {
            if (!arbeitstageDiesenMonat[i].isHoldayIn(Land.Nordrhein_Westfalen)) {
                arbeitstag_counter++;
            }
        }
        return arbeitstag_counter;
    }

    private Schichtarbeitstag[] getArbeitstagArray(int monat) {
        Schichtarbeitstag[] arbeitstageDiesenMonat = new Schichtarbeitstag[(monatsArbeitstage[monat - 1][1] - monatsArbeitstage[monat - 1][0]) + 1];
        int count = 0;
        for (int i = monatsArbeitstage[monat - 1][0]; i <= monatsArbeitstage[monat - 1][1]; i++) {
            Schichtarbeitstag temp_tag = alleTage[i];
            arbeitstageDiesenMonat[count] = temp_tag;
            count++;
        }
        return arbeitstageDiesenMonat;
    }

    private void lagerbestandworkflow(Kalenderwoche kw) {
        while (kw.next()) {
            Schichtarbeitstag tag = kw.get();
            tag.lagerbestandBerechnen(tag.getVortag());
        }
        kw.reset();
    }

    private Schichtarbeitstag starttagberechnen(Komponenttyp ktyp, Schichtarbeitstag ankunftstag) {

        Schichtarbeitstag startTag = ankunftstag;
        if (ktyp == Komponenttyp.GABEL) {
            for (int i = 0; i < 9; i++) {
                startTag = startTag.getVortag();
            }
            int vorlaufzeit = 0;
            while (vorlaufzeit != 14) {
                startTag = startTag.getVortag();
                if (!startTag.isHoldayIn(Land.Spanien)) {
                    vorlaufzeit++;
                }
            }
        } else if (ktyp == Komponenttyp.RAHMEN) {
            int vorlaufzeit = 0;
            //3 tage lieferzeit, 7 tage vorlaufzeit
            while (vorlaufzeit != (7 + 3)) {
                startTag = startTag.getVortag();
                if (!startTag.isHoldayIn(Land.Baden_Württemberg)) {
                    vorlaufzeit++;
                }
            }
        } else if (ktyp == Komponenttyp.SATTEL) {
            for (int i = 0; i < 31; i++) {
                startTag = startTag.getVortag();
            }
            int vorlaufzeit = 0;
            //7 wochen vorlaufzeit, davon 30 tage verschiffung und 18 tage (von 7*7 = 49 Tagen) in China als Arbeitstage
            while (vorlaufzeit != 18) {
                startTag = startTag.getVortag();
                if (!startTag.isHoldayIn(Land.China)) {
                    vorlaufzeit++;
                }
            }
        } else {
            throw new IllegalArgumentException("Das ergibt keinen sinn");
        }
        if (startTag.getArbeitstag_ID() <= connectivity_interface.heute()) return null;
        return startTag;
    }

    private boolean lieferungworkflow(Kalenderwoche kw) {
        Schichtarbeitstag ersterTag = kw.getFirst();//immer montag
        Schichtarbeitstag letzterArbeitstagVorwoche = ersterTag.getVortag();
        LinkedList<Schichtarbeitstag> zuBerucksichtigen = new LinkedList<>();
        if (letzterArbeitstagVorwoche != null) {
            while (letzterArbeitstagVorwoche.isHoldayIn(Land.Nordrhein_Westfalen)) {
                zuBerucksichtigen.add(letzterArbeitstagVorwoche);
                letzterArbeitstagVorwoche = letzterArbeitstagVorwoche.getVortag();
            }
        } else {
            letzterArbeitstagVorwoche = ersterTag;
        }
        Schichtarbeitstag letzterTag = kw.getLast();

        for (int i = 9; i <= 22; i++) {
            if (letzterTag.getLagerbestand(i) < Database_Connectivity_Interface.getPuffer() && letzterTag.getArbeitstag_ID() > 115) {
                Schichtarbeitstag starttag = starttagberechnen(Komponentenzuordnung.getKtypFromID(i), letzterArbeitstagVorwoche);
                if (starttag == null) return false;
                Lieferung neededLieferung = new Lieferung(i, letzterTag.getNeededAmount(i) + Database_Connectivity_Interface.getPuffer(), letzterArbeitstagVorwoche.getArbeitstag_ID(), starttag.getArbeitstag_ID(), letzterArbeitstagVorwoche.getDatum(), starttag.getDatum());
                letzterArbeitstagVorwoche.addLieferung(neededLieferung);
            }
        }

        while (zuBerucksichtigen.size() > 0) {
            Schichtarbeitstag tag = zuBerucksichtigen.removeLast();
            tag.lagerbestandBerechnen(tag.getVortag());
        }
        return true;
    }

    private void schichtenworkflow() {
        produktionUmverteilen();
        for (int x = 0; x < alleTage.length; x++) {
            alleTage[x].checkMyself();
        }
        schichtenEintragen(kwList);
    }

    private void schichtenEintragen(List<Kalenderwoche> fullmap) {
        for (Kalenderwoche kalenderwoche : fullmap) {
            queryA.setSchichten(kalenderwoche);
            queryA.setVorgabetag(kalenderwoche);
        }
    }


    //verteilung anhand von monatsvorgaben, sodass jede woche höchstens die Grenze für eine Schicht erreicht, ausser die zweite woche des monats,
    //welche dann den ganzen rest auffängt und mit mehr schichten produziert
    private void produktionUmverteilen() {
        for (int month = 1; month <= 12; month++) {
            Schichtarbeitstag ersterTag = alleTage[monatsArbeitstage[month - 1][0]];
            Schichtarbeitstag letzterTag = alleTage[monatsArbeitstage[month - 1][1]];
            int KWIDStart = ersterTag.getKwID();
            int KWIDEnde = letzterTag.getKwID();
            List<Kalenderwoche> relevantKW = new LinkedList<Kalenderwoche>();
            //in den kalenderwochen sind tage, die für den monat nicht bestimmt sind.
            //um die Zielvorgaben zu wahren, wird hier aussortiert.
            for (Kalenderwoche k : kwList) {
                if (k.getKwID() >= KWIDStart && k.getKwID() <= KWIDEnde) {
                    Kalenderwoche kOnlyThisMonth = new Kalenderwoche(k.getKwID());
                    while (k.next()) {
                        Schichtarbeitstag s = k.get();
                        if (s.getArbeitstag_ID() >= ersterTag.getArbeitstag_ID() && s.getArbeitstag_ID() <= letzterTag.getArbeitstag_ID()) {
                            kOnlyThisMonth.add(s);
                        }
                    }
                    k.reset();
                    kOnlyThisMonth.reset();
                    relevantKW.add(kOnlyThisMonth);
                }
            }

            int[] minmax = minMax(relevantKW);
            // aus allen arbeitstage des monats, aufgeteilt auf KW, die überschüssigen Planbedarfe subtrahieren und bei seite legen, hier limit noch 520
            int[] fahrradsums = new int[8];
            for (int fahrradid = 0; fahrradid < 8; fahrradid++) {
                for (Kalenderwoche kalenderwoche : relevantKW) {
                    while (kalenderwoche.next()) {
                        Schichtarbeitstag arbeitstag = kalenderwoche.get();
                        fahrradsums[fahrradid] = fahrradsums[fahrradid] + arbeitstag.extractDeficit(fahrradid);
                    }
                    kalenderwoche.reset();
                }
            }

            //für alle Wochen, die mindestens kw2 des monats sind limit erhöhen und defizite addieren
            for (Kalenderwoche kw : relevantKW) {
                if (kw.getKwID() != minmax[0]) {
                    int schichtIDKWMax = 0;
                    while (kw.next()) {
                        Schichtarbeitstag arbeitstag = kw.get();
                        fahrradsums = arbeitstag.addWeeksDeficits(fahrradsums, 1560);
                        arbeitstag.limitAnpassen(520);
                        if (arbeitstag.getSchicht_ID() > schichtIDKWMax) {
                            schichtIDKWMax = arbeitstag.getSchicht_ID();
                        }
                    }
                    kw.reset();
                }
            }
        }
    }

    private int[] minMax(List<Kalenderwoche> integers) {
        int[] ints = new int[integers.size()];
        int index = 0;
        for (Kalenderwoche k : integers) {
            ints[index] = k.getKwID();
            index++;
        }
        int[] minmax = new int[2];
        int max = ints[0];
        int min = ints[0];
        for (int i = 0; i < ints.length; i++) {
            if (min > ints[i]) min = ints[i];
            if (max < ints[i]) max = ints[i];
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }

    //tested
    public Schichtarbeitstag[] fillProdVolume(Schichtarbeitstag[] zielvorgabenTage, int vorgabeMonat, int anzahlArbeitstage, int fahrradID) {

        if (vorgabeMonat % anzahlArbeitstage == 0) {
            for (int j = 0; j < zielvorgabenTage.length; j++) {
                Schichtarbeitstag thatDay = zielvorgabenTage[j];
                if (!thatDay.isHoldayIn(Land.Nordrhein_Westfalen)) {
                    thatDay.addVerbrauch(fahrradID, vorgabeMonat / anzahlArbeitstage);
                }
            }
        } else {
            int divisor = (int) ((double) vorgabeMonat / anzahlArbeitstage);
            int index = 0;
            int copy = vorgabeMonat;
            while (copy != 0) {
                if (!zielvorgabenTage[index].isHoldayIn(Land.Nordrhein_Westfalen)) {
                    if (copy < divisor) {
                        divisor = copy;
                    }
                    zielvorgabenTage[index].addVerbrauch(fahrradID, divisor);
                    copy -= divisor;
                }
                index = ((++index) % zielvorgabenTage.length);
            }
        }
        return zielvorgabenTage;
    }


    public void changeTable(String executeQuery) {
        connectivity_interface.changeTable(executeQuery);
    }

    public String getToday() {
        if (connectivity_interface == null) {
            return "01.01.1999";
        }
        return connectivity_interface.getToday();
    }

    public int getToday(int a) {
        return connectivity_interface.heute();
    }

    public Data_Manipulation_Interface getManInterface() {
        return dataInferface;
    }

    public void commit() {
        connectivity_interface.comnmit();
    }

    public Bestellung bestellungeinbauen(int liefertag_ID, int modelltyp_ID, int anzahl) {
        System.out.println("        Bestellung wird überprüft");
        //eine neue Bestellung geht ein, wir holen uns den tag, an dem sie ankommen soll
        Schichtarbeitstag tag = null;
        //bestellung objekt initialisieren
        Bestellung neueBestellung = new Bestellung(this.getToday(1), liefertag_ID, modelltyp_ID, anzahl, this.getToday(), getTagFromID(liefertag_ID).getDatum());
        try {
            tag = alleTage[liefertag_ID];//indexoutofbounds wenn tag nicht exisitert
        } catch (IndexOutOfBoundsException inex) {
            return null;
        }
        tag.addBestellung(neueBestellung);//gab einen, bestellung hinzufügen
        int kwId = tag.getKwID();
        for (Kalenderwoche kw : kwListCopy) {
            if (kw.getKwID() >= kwId) {
                lagerbestandworkflow(kw);
                if (!lieferungworkflow(kw)) return null;
                bestellungworkflow(kw.getVorwoche());
                lagerbestandworkflow(kw);
            }
        }
        System.out.println("        Bestellung ist ok");
        return neueBestellung;
    }


    public int getTagIDFromString(String liefertag) {
        for (int i = 0; i < alleTage.length; i++) {
            if (alleTage[i].getDatum().equals(liefertag)) {
                return i;
            }
        }
        return -1;
    }

    public void setToday(String heute) {
        connectivity_interface.setToday(heute);
    }

    public Schichtarbeitstag getTagFromID(int today) {
        Schichtarbeitstag returnvalue = alleTage[today];
        if (returnvalue.getArbeitstag_ID() != today) throw new IllegalStateException("HEY");
        return returnvalue;
    }

    public String getAnzeigeWertFromID(int tagID) {
        return null;
    }

    public void writeInCSV() {
        String schichtartbeitstage = "CSVDATA/loadSchichtarbeitstage.txt";
        String kalenderwochen = "CSVDATA/loadKalenderwoche.txt";
        String bestellungen = "CSVDATA/loadBestellungen.txt";
        String lieferungen = "CSVDATA/loadLieferungen.txt";
        String fahrplaene = "CSVDATA/loadFahrplaene.txt";

        writeSchichtarbeitstage(schichtartbeitstage);
        writeKalenderwochen(kalenderwochen);
        writeBestellungen(bestellungen);
        writeLieferungen(lieferungen);
        writeFahrplaene(fahrplaene);
    }

    public void writeFahrplaene(String path) {
        BufferedWriter writer = getWriter(path);
        for (int i = 0; i < abfahrtstage.length; i++) {
            try {
                writer.append("" + abfahrtstage[i] + "\n");
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
            try {
                writer.close();
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        }
    }

    public void writeLieferungen(String path) {
        BufferedWriter writer = getWriter(path);
        for (int i = 0; i < alleTage.length; i++) {
            Schichtarbeitstag tag = alleTage[i];
            for (Lieferung lieferung : tag.getLieferungen()) {
                String outputString = "";
                outputString = outputString + lieferung.getKomponenttyp_ID() + ", ";
                outputString = outputString + lieferung.getAnzahl() + ", ";
                outputString = outputString + lieferung.getAnkunftstag_ID() + ", ";
                outputString = outputString + lieferung.getAnkunftstag_datum() + ", ";
                outputString = outputString + lieferung.getStarttag_ID() + ", ";
                outputString = outputString + lieferung.getStarttag_datum() + "\n ";
                try {
                    writer.append(outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    private void writeBestellungen(String path) {
        BufferedWriter writer = getWriter(path);
        for (int i = 0; i < alleTage.length; i++) {
            Schichtarbeitstag tag = alleTage[i];
            for (Bestellung bestellung : tag.getBestellungen()) {
                String outputString = "";
                outputString = outputString + bestellung.getBestell_ID() + ", ";
                outputString = outputString + bestellung.getBestellungstag() + ", ";
                outputString = outputString + bestellung.getLiefertag() + ", ";
                outputString = outputString + bestellung.getBestelldatum_ID() + ", ";
                outputString = outputString + bestellung.getLieferdatum_ID() + ", ";
                outputString = outputString + bestellung.getModelltyp_ID() + ", ";
                outputString = outputString + bestellung.getAnzahl() + "\n";
                try {
                    writer.append(outputString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    private void writeKalenderwochen(String path) {
        BufferedWriter writer = getWriter(path);
        //erstellen einen writer auf dem outputfile
        //jeden tag der kw auslesen und mit id festschreiben
        for (Kalenderwoche k : kwList) {
            String outputString = "";
            while (k.next()) {
                Schichtarbeitstag tag = k.get();
                outputString = outputString + tag.getArbeitstag_ID();
                if (tag.getArbeitstag_ID() != k.getLast().getArbeitstag_ID()) {
                    outputString = outputString + ", ";
                }
            }
            outputString = outputString + "\n";
            try {
                writer.append(outputString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }


    private void writeSchichtarbeitstage(String path) {
        BufferedWriter writer = getWriter(path);

        //pro schichtarbeitstag eine zeile
        for (int i = 0; i < alleTage.length; i++) {
            String outputstring = "";
            Schichtarbeitstag tag = alleTage[i];

            outputstring = outputstring + tag.getSchicht_ID() + ", ";
            outputstring = outputstring + tag.getMax_output() + ", ";
            outputstring = outputstring + tag.getSchicht_ID() + ", ";
            for (int x = 0; x < tag.getFahrradplan().length; x++) {
                outputstring = outputstring + tag.getFahrradplan()[x] + ", ";
            }
            outputstring = outputstring + tag.getKwID() + ", ";
            outputstring = outputstring + tag.getArbeitstag_ID() + ", ";
            if (tag.isHoldayIn(Land.China)) {
                outputstring = outputstring + "t, ";
            } else {
                outputstring = outputstring + "f, ";
            }
            if (tag.isHoldayIn(Land.Spanien)) {
                outputstring = outputstring + "t, ";
            } else {
                outputstring = outputstring + "f, ";
            }
            if (tag.isHoldayIn(Land.Baden_Württemberg)) {
                outputstring = outputstring + "t, ";
            } else {
                outputstring = outputstring + "f, ";
            }
            if (tag.isHoldayIn(Land.Nordrhein_Westfalen)) {
                outputstring = outputstring + "t, ";

            } else {
                outputstring = outputstring + "f, ";
            }
            outputstring = outputstring + tag.getDatum() + ", ";
            for (int l = 0; l < tag.getLager2().length; l++) {
                if (l == 0) {
                    outputstring = outputstring + tag.getLager2()[l];
                } else {
                    outputstring = outputstring + ", " + tag.getLager2()[l];
                }
            }
            outputstring = outputstring + "\n";
            try {
                writer.append(outputstring);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //fertig, writer schliessen
        try {
            writer.close();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public void loadFromCSV() {
        String schichtartbeitstage = "CSVDATA/loadSchichtarbeitstage.txt";
        String kalenderwochen = "CSVDATA/loadKalenderwoche.txt";
        String bestellungen = "CSVDATA/loadBestellungen.txt";
        String lieferungen = "CSVDATA/loadLieferungen.txt";
        String fahrplaene = "CSVDATA/loadFahrplaene.txt";
        loadSchichtarbeitstage(schichtartbeitstage);
        loadKalenderwoche(kalenderwochen);
        loadBestellungen(bestellungen);
        loadLieferungen(lieferungen);
        loadFahrplaene(fahrplaene);


    }

    private void loadFahrplaene(String fahrplaene) {
        List<String> strings = Database_Helper.fullFileToStringList(fahrplaene);
        abfahrtstage = new int[strings.size()];
        int index = 0;
        for (String s : strings) {
            abfahrtstage[index] = Integer.getInteger(s).intValue();
            index++;
        }
    }

    private void loadLieferungen(String lieferungen) {
        List<String> strings = Database_Helper.fullFileToStringList(lieferungen);
    }

    private void loadBestellungen(String bestellungen) {
        List<String> strings = Database_Helper.fullFileToStringList(bestellungen);
    }

    private void loadKalenderwoche(String kalenderwochen) {
        List<String> strings = Database_Helper.fullFileToStringList(kalenderwochen);
    }

    private void loadSchichtarbeitstage(String schichtartbeitstage) {
        List<String> strings = Database_Helper.fullFileToStringList(schichtartbeitstage);
        for (String s : strings) {
            String[] results = s.split(", ", 0);
            int schichtID = Integer.getInteger(results[0]).intValue();
            int max_output = Integer.getInteger(results[1]).intValue();
            int[] fahrradplan = new int[8];
            fahrradplan[0] = Integer.getInteger(results[2]).intValue();
            fahrradplan[1] = Integer.getInteger(results[3]).intValue();
            fahrradplan[2] = Integer.getInteger(results[4]).intValue();
            fahrradplan[3] = Integer.getInteger(results[5]).intValue();
            fahrradplan[4] = Integer.getInteger(results[6]).intValue();
            fahrradplan[5] = Integer.getInteger(results[7]).intValue();
            fahrradplan[6] = Integer.getInteger(results[8]).intValue();
            fahrradplan[7] = Integer.getInteger(results[9]).intValue();
            fahrradplan[7] = Integer.getInteger(results[10]).intValue();
            int kwID = Integer.getInteger(results[11]).intValue();
            int tagID =Integer.getInteger(results[12]).intValue();
            HashSet<Land> holidays = new HashSet<Land>();
            if(results[13] == "t"){
                holidays.add(Land.China);
            }
            if(results[14] == "t"){
                holidays.add(Land.Spanien);
            }
            if(results[15] == "t"){
                holidays.add(Land.Baden_Württemberg);
            }
            if(results[16] == "t"){
                holidays.add(Land.Nordrhein_Westfalen);
            }
            String datum = results[17];
            int[] lager = new int[22];
            lager[0] = Integer.getInteger(results[18]).intValue();
            lager[1] = Integer.getInteger(results[19]).intValue();
            lager[2] = Integer.getInteger(results[20]).intValue();
            lager[3] = Integer.getInteger(results[21]).intValue();
            lager[4] = Integer.getInteger(results[22]).intValue();
            lager[5] = Integer.getInteger(results[23]).intValue();
            lager[6] = Integer.getInteger(results[24]).intValue();
            lager[7] = Integer.getInteger(results[25]).intValue();
            lager[8] = Integer.getInteger(results[26]).intValue();
            lager[9] = Integer.getInteger(results[27]).intValue();
            lager[10] = Integer.getInteger(results[28]).intValue();
            lager[11] = Integer.getInteger(results[29]).intValue();
            lager[12] = Integer.getInteger(results[30]).intValue();
            lager[13] = Integer.getInteger(results[31]).intValue();
            lager[14] = Integer.getInteger(results[32]).intValue();
            lager[15] = Integer.getInteger(results[33]).intValue();
            lager[16] = Integer.getInteger(results[34]).intValue();
            lager[17] = Integer.getInteger(results[35]).intValue();
            lager[18] = Integer.getInteger(results[36]).intValue();
            lager[19] = Integer.getInteger(results[37]).intValue();
            lager[20] = Integer.getInteger(results[38]).intValue();
            lager[21] = Integer.getInteger(results[39]).intValue();
            lager[22] = Integer.getInteger(results[40]).intValue();
            Schichtarbeitstag tag = new Schichtarbeitstag(schichtID,max_output,fahrradplan,kwID,tagID,holidays,datum);
            for(int i=1; i<=lager.length; i++){
                tag.setLagerbestand(i, lager[i-1]);
            }
            alleTage[tagID] = tag;
        }
    }


    private BufferedWriter getWriter(String path) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path, true));
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return writer;
    }

}

