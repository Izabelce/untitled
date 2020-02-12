package Data_Manipulation_Module;

import Database_Connectivity_Module.Bestellung;
import Database_Connectivity_Module.Kalenderwoche;
import Database_Connectivity_Module.Lieferung;
import Database_Connectivity_Module.Schichtarbeitstag;

import java.util.LinkedList;
import java.util.List;

public class Data_Manipulation_Interface {
    private Data_Manipulation_Controller myController;
    private List<Schichtarbeitstag> relevanteAT;
    private List<Kalenderwoche> relevanteKW;
    private List<Lieferung> relevanteLieferungen;
    private List<Bestellung> relevanteBestellungen;
    private static final int LIEFERUNGENINFOLENGTH = 5;
    private static final int BESTELLUNGENINFOLENGTH = 4;
    private Lieferung[] heutigeLieferungen;
    int lieferungenZeiger;
    private Bestellung[] heutigeBestellungen;
    int bestellungenzeiger;

    /**
     * Konstruktor, setzt die internen listen
     */
    public Data_Manipulation_Interface() {
        relevanteAT = new LinkedList<Schichtarbeitstag>();
        relevanteKW = new LinkedList<Kalenderwoche>();
        relevanteLieferungen = new LinkedList<Lieferung>();
        relevanteBestellungen = new LinkedList<Bestellung>();
    }

    /**
     * wichtige methode, die den Data_Manipulation_Controller zuweist, von dem das Interface alle daten erhält
     * @param con
     */
    public void enlistController(Data_Manipulation_Controller con) {
        myController = con;
    }

    /**
     * nimmt daten für eine bestellung entgegen und übergibt die nächste mögliche bestellung
     * @param fahrradID
     * @param anzahl
     * @param liefertag
     * @return
     */
    public Bestellung neueBestellungVersuchen(int fahrradID, int anzahl, int liefertag) {
        if (Plausibility.isWrongID(fahrradID) || anzahl < 0 || myController.isDayinPast(liefertag)) return null;
        Bestellung neueBestellung = myController.bestellungeinbauen(liefertag, fahrradID, anzahl);
        if (neueBestellung != null) {
            relevanteBestellungen.add(neueBestellung);
        }
        return neueBestellung;
    }

    /**
     * ruft eine andere methode auf, nur andere signatur als diese
     * @param fahrradID
     * @param anzahl
     * @param liefertag
     * @return
     */
    public Bestellung neueBestellungVersuchen(int fahrradID, int anzahl, String liefertag) {
        return neueBestellungVersuchen(fahrradID, anzahl, myController.getTagIDFromString(liefertag));
    }

    /**
     * gibt den heutigen tag als string zurück
     * @return
     */
    public String getToday() {
        return myController.getToday();
    }

    /**
     * verändert den heutigen tag zu dem übergebenen wert
     * @param heute
     */
    public void setToday(String heute) {
        myController.setToday(heute);
    }

    /**
     * gibt zu dem zu heute gehörigen tag den korrespondierenden schichtarbeitstag zurück
     * @return
     */
    public Schichtarbeitstag getSchichtarbeitstag_heute() {
        Schichtarbeitstag relevant = myController.getTagFromID(myController.getToday(0));
        if (relevant != null) {
            relevanteAT.add(relevant);
            return relevant;
        }
        return null;
    }

    /**
     * erhöht den internen lieferungzeiger um 1, zeigt somit also auf die nächste lieferung
     *
     * @return true, wenn es eine gibt, false wenn nicht
     */
    public boolean nextLieferung() {
        lieferungenZeiger++;
        if (lieferungenZeiger >= heutigeLieferungen.length) {
            return false;
        }
        return true;
    }

    /**
     * ändert den tag, an dem wir die lieferungen betrachten wollen zum angegebenen datum
     * @param datum
     */
    public void setTagDerLieferungen(String datum) {
        Schichtarbeitstag tag = getTag(datum);
        heutigeLieferungen = tag.getLieferungen().toArray(new Lieferung[0]);
        relevanteLieferungen.addAll(tag.getLieferungen());
        lieferungenZeiger = -1;
    }

    /**
     * gibt alle infos über die momentan ausgewählte lieferung zurück
     * @return
     */
    public String[] getCurrentLieferungInfos() {
        String[] infos = new String[LIEFERUNGENINFOLENGTH];
        infos[0] = heutigeLieferungen[lieferungenZeiger].getAnkunftstag_datum();
        infos[1] = Komponentenzuordnung.getCanonicalName(heutigeLieferungen[lieferungenZeiger].getKomponenttyp_ID());

        infos[2] = Integer.toString(heutigeLieferungen[lieferungenZeiger].getAnzahl());
        infos[3] = "no";
        infos[4] = heutigeLieferungen[lieferungenZeiger].getStarttag_datum();
        return infos;
    }

    /**
     * gibt alle infos über die momentan ausgewählte bestellung zurück
     * @return
     */
    public String[] getCurrentBestellungInfos() {
        String[] infos = new String[BESTELLUNGENINFOLENGTH];

        infos[0] = heutigeBestellungen[bestellungenzeiger].getBestellungstag();
        infos[1] = heutigeBestellungen[bestellungenzeiger].getLiefertag();
        infos[2] = Komponentenzuordnung.getCanonicalName(heutigeBestellungen[bestellungenzeiger].getModelltyp_ID());

        infos[3] = Integer.toString(heutigeBestellungen[bestellungenzeiger].getAnzahl());
        return infos;
    }

    /**
     * ändert den tag, an dem wir die bestellungen betrachten wollen
     * @param datum
     */
    public void setTagderBestellungen(String datum) {
        Schichtarbeitstag tag = getTag(datum);
        heutigeBestellungen = tag.getBestellungen().toArray(new Bestellung[0]);
        relevanteBestellungen.addAll(tag.getBestellungen());
        bestellungenzeiger = -1;
    }

    /**
     * erhöht den internen zeiger für das bestellungen array um 1, geth also auf die nächste bestellung
     * @return true, wenn es eine gibt, false wenn nicht
     */
    public boolean nextBestellung() {
        bestellungenzeiger++;
        if (bestellungenzeiger >= heutigeBestellungen.length) {
            return false;
        }
        return true;
    }

    /**
     * gibt zu einem bestimmten String Datum den dazugehörigen Schichtarbeitstag zurück
     * @param datum
     * @return
     */
    private Schichtarbeitstag getTag(String datum) {
        int id = myController.getTagIDFromString(datum);
        Schichtarbeitstag tag = null;
        for (Schichtarbeitstag t : relevanteAT) {
            if (t.getArbeitstag_ID() == id) {
                tag = t;
            }
        }
        if (tag == null) {
            tag = myController.getTagFromID(id);
        }
        return tag;
    }

    /**
     * Gibt ein String array über die jeweiligen lagerbestände des tages zurück
     * Hat insgesamt 23 indize, 1 für das datum des tages, 22 für die lagerobjekte
     */
    public String[] getLagerbestandTag(int datum) {
        String[] bestand = new String[23];
        Schichtarbeitstag tag = myController.getAlleTage()[datum];
        bestand[0] = tag.getDatum();
        for (int i = 1; i < tag.getLager2().length; i++) {
            bestand[i] = Integer.toString(tag.getLager2()[i - 1]);
        }
       return bestand;
    }

    /**
     * Gibt ein array über alle Lagerbestände innerhalb der zwei angegebenen daten zurück
     * jedes array hat 23 indize, 1 für das datum des tages, 22 für die lagerobjekte
     * die dimension des ersten arrays ergibt sich aus den tagen, die zwischen datum 1 und 2 liegen (inklusive)
     * @param datumErsterTag
     * @param datumLetzterTag
     * @return
     */
    public String[][] getLagerbestandInRange(String datumErsterTag, String datumLetzterTag) {
        int erstertag = myController.getTagIDFromString(datumErsterTag);
        int letzterTag = myController.getTagIDFromString(datumLetzterTag);
        String[][] returnvalue = new String[(letzterTag - erstertag) + 1][23];
        Schichtarbeitstag tag = myController.getAlleTage()[letzterTag];
        for (int i = returnvalue.length - 1; i >= 1; i--) {
            returnvalue[i] = getLagerbestandTag(tag.getArbeitstag_ID());
            tag = tag.getVortag();
        }
        return returnvalue;
    }

    /**
     * gibt die Summe der insgesamt im zeitraum vorgesehenen produzierten fahrräder zurück
     * @param datumErsterTag
     * @param datumLetzterTag
     * @return
     */
    public String getGesamtzahlProduktionInRange(String datumErsterTag, String datumLetzterTag) {
        int erstertagID = myController.getTagIDFromString(datumErsterTag);
        int letztertagID = myController.getTagIDFromString(datumLetzterTag);

        int sum = 0;
        for (int i = erstertagID; i <= letztertagID; i++) {
            Schichtarbeitstag tag = myController.getAlleTage()[i];
            for (int j = 0; j < tag.getFahrradplan().length; j++) {
                sum = sum + tag.getFahrradplan()[j];
            }
        }
        return Integer.toString(sum);
    }

    /**
     * gibt die gesamtzahl der produktion, also die summe aller geplanten fahrräder, an einem tag wieder
     * @param datum
     * @return
     */
    public String getGesamtzahlProduktionDay(String datum) {
        return getGesamtzahlProduktionInRange(datum, datum);
    }

    /**
     * Gibt die summe aller in der Woche, zu der der Tag gehört, zur produktion vorgesehenen fahrräder zurück
     * @param datum
     * @return
     */
    public String getGesamtzahlProduktionWeek(String datum) {
        int tagId = myController.getTagIDFromString(datum);
        Kalenderwoche kw = myController.getKwList()[myController.getAlleTage()[tagId].getKwID()];

        return getGesamtzahlProduktionInRange(kw.getFirst().getDatum(), kw.getLast().getDatum());
    }

    /**
     * gibt die gesamtzahl aller in dem monat, zu dem der tag gehört, zur produktion vorgesehenen fahrräder zurück
     * @param datum
     * @return
     */
    public String getGesamtzahlProduktionMonth(String datum) {
       String[] taginfos = getDatumMonthEnden(datum);
        return getGesamtzahlProduktionInRange(taginfos[0], taginfos[1]);
    }

    /**
     * Gibt die gesamte anzahl von zur Produktion vorgesehenen Fahrräder im Jahr 2021 zurück
     * @return
     */
    public String getGesamtProduktionJahr(){
        return getGesamtzahlProduktionInRange(myController.getAlleTage()[0].getDatum(), myController.getAlleTage()[myController.getAlleTage().length-1].getDatum());
    }

    /**
     * Gibt ein array über alle sekundärbedarfe im angegebenen zeitraum zurück, das sind lagerobjekte 9 bis 21
     * letzter eintrag ist immer die summen
     * @return
     */
    public String[][] getsekundarBedarfInRange(String datumErsterTag, String datumLetzterTag){
        int ersterTagID = myController.getTagIDFromString(datumErsterTag);
        int letzterTagID = myController.getTagIDFromString(datumLetzterTag);

        String[][] bedarfe = new String[letzterTagID-ersterTagID +2][15];
        int[] sums = new int[14];
        String[] summen = new String[15];
        summen[0] = "SUMMEN:";
        int id= ersterTagID;
        for(int i=0; i<bedarfe.length; i++){
            for(int j =0; j< myController.getAlleTage()[id].getSekundarBedarfe().length; j++){
                sums[j] = sums[j] + myController.getAlleTage()[id].getSekundarBedarfe()[j];
            }
            bedarfe[i] = myController.getAlleTage()[id].getSekundarBedarfe((String[])null);
            id++;
        }
        for(int l = 1; l<15; l++){
            summen[l] = Integer.toString(sums[l]);
        }
        bedarfe[bedarfe.length-1] = summen;
        return bedarfe;
    }

    public String[][] getSekundarBedarfTag(String datum){
        return getsekundarBedarfInRange(datum, datum);
    }

    public String[][] getSekundarbedarfWoche(String datum){
        Kalenderwoche kw = myController.getKwList()[myController.getAlleTage()[myController.getTagIDFromString(datum)].getKwID()];
        return getsekundarBedarfInRange(kw.getFirst().getDatum(), kw.getLast().getDatum());
    }

    public String[][] getSekundarbedarfMonat(String datum){
       String[] taginfos = getDatumMonthEnden(datum);
        return getsekundarBedarfInRange(taginfos[0], taginfos[1]);
    }

    private String[] getDatumMonthEnden(String datum){
        String[] returnvalue = new String[2];
        int tagID =myController.getTagIDFromString(datum);
        Schichtarbeitstag tag  =myController.getAlleTage()[tagID];
        int monthID = tag.getMonats_ID();
        while(tag.getVortag().getMonats_ID() == monthID){
            tag = tag.getVortag();
        }
        String ersterTag = tag.getDatum();
        while(myController.getAlleTage()[tagID].getMonats_ID() == monthID){
            tagID++;
        }
        String lezterTag = myController.getAlleTage()[tagID-1].getDatum();
        returnvalue[0] = ersterTag;
        returnvalue[1] = lezterTag;
        return returnvalue;
    }

    public String[][] getSekundarbedarfeJahr(){
        return  getsekundarBedarfInRange(myController.getAlleTage()[0].getDatum(), myController.getAlleTage()[myController.getAlleTage().length-1].getDatum());
    }

    public String[][] getProduktionsplanungInRange(String datumErsterTag, String datumLetzterTag){
        int ersterTagID = myController.getTagIDFromString(datumErsterTag);
        int letzterTagID = myController.getTagIDFromString(datumLetzterTag);
        String[][] returnvalue = new String[letzterTagID-ersterTagID+1][9];
        int id= ersterTagID;
        for(int i =0; i< returnvalue.length; i++){
            returnvalue[i] = myController.getAlleTage()[id].getFahrradplanStrings();
        }
        return returnvalue;
    }

    public String[][] getProduktionsplanungTag(String datum){
        return getProduktionsplanungInRange(datum, datum);
    }

    public String[][] getProduktionsplanungWoche(String datum){
        Kalenderwoche kw = myController.getKwList()[myController.getAlleTage()[myController.getTagIDFromString(datum)].getKwID()];
        return getProduktionsplanungInRange(kw.getFirst().getDatum(), kw.getLast().getDatum());
    }

    public String[][] getProduktionsplanungMonat(String datum){
        String[] monatsdaten = getDatumMonthEnden(datum);
        return getProduktionsplanungInRange(monatsdaten[0], monatsdaten[1]);
    }

    public String[][] getProduktionsplanungJahr(String datum){
        return getProduktionsplanungInRange(myController.getAlleTage()[0].getDatum(), myController.getAlleTage()[myController.getAlleTage().length-1].getDatum());
    }


    /**
     * gibt die anzahl der an dem tag vorgesehenen lieferungen zurück
     * @return
     */
    public int getLieferAnzahl() {
        return heutigeLieferungen.length;
    }

    /**
     * gibt die anzahl der an dem tag vorgesehenen bestellungen zurück
     * @return
     */
    public int getBestellanzahl() {
        return heutigeBestellungen.length;
    }

    /**
     * gibt den festern wert für die länge einer lieferungsinfo (also array länge) zurück
     * @return
     */
    public int getLieferungeninfolength() {
        return LIEFERUNGENINFOLENGTH;
    }

    /**
     * gibt den festern wertfür die länge einer bestellungsinfo (also array länge) zurück
     * @return
     */
    public int getBestellungeninfolength() {
        return BESTELLUNGENINFOLENGTH;
    }
}
