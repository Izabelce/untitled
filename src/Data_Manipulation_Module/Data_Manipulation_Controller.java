package Data_Manipulation_Module;

import Database_Connectivity_Module.Database_Connectivity_Interface;
import Database_Connectivity_Module.Bestellung;
import Database_Connectivity_Module.Kalenderwoche;
import Database_Connectivity_Module.Lieferung;
import Database_Connectivity_Module.Schichtarbeitstag;

import java.sql.ResultSet;
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
    private List<Schichtarbeitstag> alleTage;
    private int[][] monatsArbeitstage;
    List<Kalenderwoche> kwList;
    private List<Schichtarbeitstag> candidates;
    private List<Kalenderwoche> kwListCopy;

    public Data_Manipulation_Controller(Database_Connectivity_Interface conI) {
        mInf = new MetricsInterface();
        plausi = new Plausibility();
        queryA = new QueryAssembler();
        queryA.enlistController(this);
        dataInferface = new Data_Manipulation_Interface();
        dataInferface.enlistController(this);
        this.connectivity_interface = conI;
         alleTage = prepareListofAllDays();
         monatsArbeitstage = prepareMonatsarbeitstage();
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
        return tagID < queryA.getIDDay(connectivity_interface.getToday());
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

    private int getPreviousWorkingDay(int tagID) {
        int wdID = tagID - 1;
        while (isHoliday(wdID)) {
            wdID--;
        }
        if (isDayinPast(wdID)) return -1;
        return wdID;
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
        for (Schichtarbeitstag s : alleTage) {
            s.checkMyself();
        }
        System.out.println("    Berechnete Werte werden festgeschrieben...");
        //TODO KOMMENTARE ENTFERNEN
        lagerbestandfestschreiben();
        for (Schichtarbeitstag s : alleTage) {
            for (Lieferung l : s.getLieferungen()) {
                   queryA.lieferungfestschrieben(l);
            }
            for (Bestellung b : s.getBestellungen()) {
                  queryA.bestellungenfestschreiben(b);
            }
        }
        candidatesherstellen();
    }

    private void candidatesherstellen() {
        candidates = new LinkedList<Schichtarbeitstag>();
        Schichtarbeitstag vortag = null;
        for(Schichtarbeitstag ursprung: alleTage){
            Schichtarbeitstag copy = ursprung.getCopy();
            copy.setVortag(vortag);
            candidates.add(copy);
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
                    tag.addBestellung(new Bestellung(tag.getArbeitstag_ID(), tag.getArbeitstag_ID(), i, tag.getLagerbestand(i)));
                }
            }
            while (zuBerucksichtigen.size() > 0) {
                Schichtarbeitstag berucksichtigt = zuBerucksichtigen.removeLast();
                berucksichtigt.lagerbestandBerechnen(berucksichtigt.getVortag());
            }
        }
    }

    private void lagerbestandfestschreiben() {
        for (Schichtarbeitstag schichtarbeitstag : alleTage) {
            queryA.setLagerbestand(schichtarbeitstag);
        }

    }

    /**
     * zieht aus der Datenbank alle Tage und setzt sie in die benötigte Datenstruktur
     *
     * @return eine Liste von Schichtarbeitstagen
     */
    private List<Schichtarbeitstag> prepareListofAllDays() {
        List<Schichtarbeitstag> returnlist = new LinkedList<Schichtarbeitstag>();
        Schichtarbeitstag tag = null;
        Schichtarbeitstag vortag = null;
        for (int tagId = 1; tagId <= 519; tagId++) {
            tag = queryA.getSchicht(tagId);
            tag.setVortag(vortag);
            vortag = tag;
            returnlist.add(tag);
        }
        return returnlist;
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
            Schichtarbeitstag temp_tag = alleTage.get(i - 1);
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

    private void muell() {//?
        Schichtarbeitstag vortag = null;
        Schichtarbeitstag letzterArbeitstag = null;
        Schichtarbeitstag letzterArbeitstagVorwoche = null;
        for (Kalenderwoche kalenderwoche : kwList) {
            while (kalenderwoche.next()) {
                Schichtarbeitstag tag = kalenderwoche.get();
                tag.lagerbestandBerechnen(vortag);
                vortag = tag;
                if (!tag.isHoldayIn(Land.Nordrhein_Westfalen)) letzterArbeitstag = tag;
            }
            kalenderwoche.reset();
            for (int i = 9; i <= 22; i++) {
                int bedarf = vortag.getNeededAmount(i); //ERROR HERE GEWESEN!
                Komponenttyp ktyp = null;
                if (bedarf > 0) {
                    switch (i) {
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            bedarf = bedarf + Komponentenzuordnung.getMBW(Komponenttyp.SATTEL);
                            ktyp = Komponenttyp.SATTEL;
                            break;

                        case 13:
                        case 14:
                        case 15:
                            bedarf = bedarf + Komponentenzuordnung.getMBW(Komponenttyp.RAHMEN);
                            ktyp = Komponenttyp.RAHMEN;
                            break;
                        default:
                            bedarf = bedarf + Komponentenzuordnung.getMBW(Komponenttyp.GABEL);
                            ktyp = Komponenttyp.GABEL;
                            break;
                    }
                    bedarf = bedarf + Database_Connectivity_Interface.getPuffer();
                }
                if (bedarf >= Komponentenzuordnung.getMBW(ktyp) && Komponentenzuordnung.getMBW(ktyp) > 0) {
                    Lieferung lieferung = new Lieferung(i, bedarf, letzterArbeitstagVorwoche.getArbeitstag_ID(), starttagberechnen(ktyp, letzterArbeitstagVorwoche));
                    letzterArbeitstagVorwoche.addLieferung(lieferung);
                }
            }
            letzterArbeitstagVorwoche = letzterArbeitstag;
        }
        vortag = null;
        for (Kalenderwoche kalenderwoche : kwList) {
            while (kalenderwoche.next()) {
                Schichtarbeitstag tag = kalenderwoche.get();
                tag.lagerbestandBerechnen(vortag);
                vortag = tag;
            }
        }
    }

    private int starttagberechnen(Komponenttyp ktyp, Schichtarbeitstag ankunftstag) {
        int starttag = -1;
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
        if (startTag.getArbeitstag_ID() <= connectivity_interface.heute()) return -1;
        return startTag.getArbeitstag_ID();
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
            if (letzterTag.getLagerbestand(i) < 0) {
                int starttagId = starttagberechnen(Komponentenzuordnung.getKtypFromID(i), letzterArbeitstagVorwoche);
                if(starttagId < getToday(0))return false;
                Lieferung neededLieferung = new Lieferung(i, letzterTag.getNeededAmount(i) + Database_Connectivity_Interface.getPuffer(), letzterArbeitstagVorwoche.getArbeitstag_ID(), starttagId);
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
        for (Schichtarbeitstag t : alleTage) {
            t.checkMyself();
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
            Schichtarbeitstag ersterTag = alleTage.get(monatsArbeitstage[month - 1][0]);
            Schichtarbeitstag letzterTag = alleTage.get(monatsArbeitstage[month - 1][1]);
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

    public boolean bestellungeinbauen(Bestellung neueBestellung) {
        Schichtarbeitstag tag = null;
        for(Schichtarbeitstag potTag: candidates){
            tag = potTag;
            if(potTag.getArbeitstag_ID() == neueBestellung.getLieferdatum_ID())break;
        }
        if(tag == null)return false;
        tag.addBestellung(neueBestellung);
        int kwId = tag.getKwID();
        for(Kalenderwoche kw: kwListCopy){
            if(kw.getKwID()>= kwId){
                lagerbestandworkflow(kw);
                if(! lieferungworkflow(kw))return false;
                bestellungworkflow(kw.getVorwoche());
                lagerbestandworkflow(kw);
            }
        }
        for(Schichtarbeitstag s: candidates){
            s.checkMyself();
        }
        return true;
    }

    public int getTagIDFromString(String liefertag) {
        return queryA.getIDDay(liefertag);
    }
}

