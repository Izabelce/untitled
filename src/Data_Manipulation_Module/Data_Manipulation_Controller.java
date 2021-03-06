package Data_Manipulation_Module;

import Database_Connectivity_Module.*;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static java.lang.StrictMath.max;


public class Data_Manipulation_Controller {
    private MetricsInterface mInf;
    private Plausibility plausi;
    private QueryAssembler queryA;
    private Data_Manipulation_Interface dataInferface;
    private Database_Connectivity_Interface connectivity_interface;
    //private List<Schichtarbeitstag> alleTage;
    private Schichtarbeitstag[] alleTage;
    private int[][] monatsArbeitstage;
    Kalenderwoche[] kwList;
    private Schichtarbeitstag[] candidates;
    private Kalenderwoche[] kwListCopy;
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
        this.kwList = Kalenderwoche.getKWListFromTage(alleTage);
        monatsArbeitstage = prepareMonatsarbeitstage();
        //kwList = null;
        ZuliefererManager.enlistController(this);
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
        //this.kwList = Kalenderwoche.getKWListFromTage(alleTage);
        //kwList = null;
        ZuliefererManager.enlistController(this);
    }


    public void enlistDatabaseConnectivity_Interface(Database_Connectivity_Interface database_connectivity_interface) {
        this.connectivity_interface = database_connectivity_interface;
    }

    private void fahrplanGenerieren() {
        abfahrtstage = queryA.getFahrplanTage();
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

        System.out.println("    Schichten werden berechnet...");
        schichtenworkflow();
        //schichtenStabilisieren();

        System.out.println("    Lagerbestände werden berechnet...");

        for (int tag = 1; tag < alleTage.length; tag++) {
            alleTage[tag].sekundarbedarfBerechnen();
            lagerbestandworkflow(alleTage[tag]);//
        }// TODO täglich statt wöchentlich
        //lieferungworkflow(kw);
        System.out.println("    Lieferungen werden berechnet...");
        for (int tag = 1; tag < alleTage.length; tag++) {
            testLieferungenWorkflow(alleTage[tag]);
        }
        System.out.println("    Bestellungen werden berechnet...");
        for (int tag = 1; tag < alleTage.length; tag++) {
            bestellungworkflow(alleTage[tag]);
        }
        System.out.println("    Lagerbestände werden erneut berechnet...");
        for (int tag = 1; tag < alleTage.length; tag++) {
            lagerbestandworkflow(alleTage[tag]);
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

    void candidatesherstellen() {
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

    private void bestellungworkflow(Schichtarbeitstag tag) {

        if (!tag.isHoldayIn(Land.Nordrhein_Westfalen)) {
            for (int i = 0; i < 8; i++) {
                if (tag.getFahrradplan()[i] != 0) {
                    Bestellung b = new Bestellung(getToday(0), tag.getArbeitstag_ID(), i + 1, tag.getFahrradplan()[i], getToday(), tag.getDatum());
                    tag.addBestellung(b);
                }
            }

        }
        //LinkedList<Schichtarbeitstag> zuBerucksichtigen = new LinkedList<>();
        //while (tag.isHoldayIn(Land.Nordrhein_Westfalen)) {
        //    zuBerucksichtigen.add(tag);
        //    tag = tag.getVortag();
        //}
        //if (tag.getsum() > 0) {
        //    for (int i = 1; i <= 8; i++) {
        //        tag.addBestellung(new Bestellung(tag.getArbeitstag_ID(), tag.getArbeitstag_ID(), i, tag.getLagerbestand(i), this.getToday(), tag.getDatum()));
        //    }
        //}
        //while (zuBerucksichtigen.size() > 0) {
        //    Schichtarbeitstag berucksichtigt = zuBerucksichtigen.removeLast();
        //    berucksichtigt.lagerbestandBerechnen(berucksichtigt.getVortag());
        //}
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
        Schichtarbeitstag nexttag = null;
        vortag.setVortag(null);
        alleTage[0] = vortag;
        for (int tagId = 1; tagId < alleTage.length; tagId++) {
            tag = queryA.getSchicht(tagId);
            tag.setVortag(vortag);
            vortag.setNextTag(tag);
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

    private void lagerbestandworkflow(Schichtarbeitstag kw) {
        //Schichtarbeitstag tag = kw;
        kw.lagerbestandBerechnen(kw.getVortag());
        // // for (int i = kw.getArbeitstag_ID(); i < alleTage.length; i++) {
        //      tag.lagerbestandBerechnen(tag.getVortag());
        //      tag = tag.getNextTag();
        //  }
    }

    //private Schichtarbeitstag starttagberechnen(Komponenttyp ktyp, Schichtarbeitstag ankunftstag) {

    //    Schichtarbeitstag startTag = ankunftstag;
    //    if (ktyp == Komponenttyp.GABEL) {
    //        for (int i = 0; i < 9; i++) {
    //            startTag = startTag.getVortag();
    //        }
    //        int vorlaufzeit = 0;
    //        while (vorlaufzeit != 14) {
    //            startTag = startTag.getVortag();
    //            if (!startTag.isHoldayIn(Land.Spanien)) {
    //                vorlaufzeit++;
    //            }
    //        }
    //    } else if (ktyp == Komponenttyp.RAHMEN) {
    //        int vorlaufzeit = 0;
    //        //3 tage lieferzeit, 7 tage vorlaufzeit
    //        while (vorlaufzeit != (7 + 3)) {
    //            startTag = startTag.getVortag();
    //            if (!startTag.isHoldayIn(Land.Baden_Württemberg)) {
    //                vorlaufzeit++;
    //            }
    //        }
    //    } else if (ktyp == Komponenttyp.SATTEL) {
    //        for (int i = 0; i < 31; i++) {
    //            startTag = startTag.getVortag();
    //        }
    //        int vorlaufzeit = 0;
    //        //7 wochen vorlaufzeit, davon 30 tage verschiffung und 18 tage (von 7*7 = 49 Tagen) in China als Arbeitstage
    //        while (vorlaufzeit != 18) {
    //            startTag = startTag.getVortag();
    //            if (!startTag.isHoldayIn(Land.China)) {
    //                vorlaufzeit++;
    //            }
    //        }
    //    } else {
    //        throw new IllegalArgumentException("Das ergibt keinen sinn");
    //    }
    //    if (startTag.getArbeitstag_ID() <= connectivity_interface.heute()) return null;
    //    return startTag;
    //}

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
            if (letzterTag.getLagerbestand(i) < Database_Connectivity_Interface.getPuffer(ZuliefererManager.getZulieferer(i).getMyLand()) && letzterTag.getArbeitstag_ID() > 115) {
                //Schichtarbeitstag starttag = starttagberechnen(Komponentenzuordnung.getKtypFromID(i), letzterArbeitstagVorwoche);
                //if (starttag == null) return false;
                int bedarf = max(ZuliefererManager.getZulieferer(i).getMindestLiefermenge(), letzterTag.getNeededAmount(i) + Database_Connectivity_Interface.getPuffer(ZuliefererManager.getZulieferer(i).getMyLand()));
                Lieferung lieferung = ZuliefererManager.getZulieferer(i).neueLieferungAnlegen(i, bedarf, letzterTag.getArbeitstag_ID());
                // Lieferung neededLieferung = new Lieferung(
                //         i,
                //        // letzterTag.getNeededAmount(i) + Database_Connectivity_Interface.getPuffer(ZuliefererManager.getZulieferer(i).getMyLand()),
                //         //ZuliefererManager.getZulieferer(i).getMindestLiefermenge(),
                //         bedarf,
                //         letzterArbeitstagVorwoche.getArbeitstag_ID(), starttag.getArbeitstag_ID(),
                //         letzterArbeitstagVorwoche.getDatum(),
                //         starttag.getDatum());
                if (lieferung == null) return false;
                letzterArbeitstagVorwoche.addLieferung(lieferung);
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

    private void schichtenEintragen(Kalenderwoche[] fullmap) {
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
        // for (Kalenderwoche kw : kwListCopy) {
        //     if (kw.getKwID() >= kwId) {//TODO
        //         lagerbestandworkflow(kw);
        //         if (!lieferungworkflow(kw)) return null;
        //         bestellungworkflow(kw.getVorwoche());
        //         lagerbestandworkflow(kw);
        //     }
        // }
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


    public void setAlleTage(Schichtarbeitstag[] alleTage) {
        this.alleTage = alleTage;
    }

    public void setKwList(Kalenderwoche[] kwList) {
        this.kwList = kwList;
    }


    public void setAbfahrtstage(int[] abfahrtstage) {
        this.abfahrtstage = abfahrtstage;
    }

    public MetricsInterface getmInf() {
        return mInf;
    }

    public Schichtarbeitstag[] getAlleTage() {
        return alleTage;
    }

    public Kalenderwoche[] getKwList() {
        return kwList;
    }

    /**
     * sdsdsdssd
     *
     * @return
     */
    public int[] getAbfahrtstage() {
        return abfahrtstage;
    }

    /**
     * alternative methode die lieferungen zu berechnen
     * wenn eine lieferung nicht möglich ist, wird die mögliche produktionsmenge des fahrrads auf 0 gesetzt und auf den nächsten tag gelegt usw.
     */
    private void testLieferungenWorkflow(Schichtarbeitstag tag) {

        int[] sekundarBedarfTag = tag.getSekundarBedarfe();
        Schichtarbeitstag vorherigerArbeitsTag = tag.getVortag();
        while (vorherigerArbeitsTag.isHoldayIn(Land.Nordrhein_Westfalen)) {
            vorherigerArbeitsTag = vorherigerArbeitsTag.getVortag();
        }
        int vorHerigerarbeitstagID = vorherigerArbeitsTag.getArbeitstag_ID();
        Schichtarbeitstag nextArbeitstag = tag.getNextTag();

        while (nextArbeitstag != null && nextArbeitstag.isHoldayIn(Land.Nordrhein_Westfalen)) {
            nextArbeitstag = nextArbeitstag.getNextTag();
        }
        //berechnen
        for (int j = 0; j < sekundarBedarfTag.length; j++) {
            int komponentID = j + 9;
            if (sekundarBedarfTag[j] > 0) {
                Zulieferer zulieferer = ZuliefererManager.getZulieferer(komponentID);
                Lieferung lieferung = zulieferer.neueLieferungAnlegen(komponentID, sekundarBedarfTag[j], vorHerigerarbeitstagID);
                if (lieferung != null) {
                    vorherigerArbeitsTag.addLieferung(lieferung);
                } else {
                    //betroffene fahrräder berechnen und rückstand für den tag ermitteln (alle fahrräder des typs)
                    int[] betroffeneBikes = Komponentenzuordnung.getFahrräderZurKomponente(komponentID);
                    for (int betroffeneID = 0; betroffeneID < betroffeneBikes.length; betroffeneID++) {
                        tag.setRuckstand(betroffeneBikes[betroffeneID] - 1, tag.getFahrradplan()[betroffeneBikes[betroffeneID] - 1]);
                        //fahrradplan anpassen
                        tag.changeFahrplan(betroffeneBikes[betroffeneID], 0);
                    }

                    //rückstand auf nächsten tag schieben
                    if (nextArbeitstag != null) {

                        for (int betroffeneID = 0; betroffeneID < betroffeneBikes.length; betroffeneID++) {
                            int vorher = nextArbeitstag.getFahrradplan()[betroffeneBikes[betroffeneID] - 1];
                            int neu = vorher + tag.getRuckstand()[betroffeneBikes[betroffeneID] - 1];
                            nextArbeitstag.changeFahrplan(betroffeneBikes[betroffeneID], neu);
                        }
                    }
                }
            }

        }

    }
}



