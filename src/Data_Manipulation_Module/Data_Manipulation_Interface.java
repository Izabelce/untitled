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

    public Data_Manipulation_Interface() {
        relevanteAT = new LinkedList<Schichtarbeitstag>();
        relevanteKW = new LinkedList<Kalenderwoche>();
        relevanteLieferungen = new LinkedList<Lieferung>();
        relevanteBestellungen = new LinkedList<Bestellung>();
    }

    public void enlistController(Data_Manipulation_Controller con) {
        myController = con;
    }

    public Bestellung neueBestellungVersuchen(int fahrradID, int anzahl, int liefertag) {
        if (Plausibility.isWrongID(fahrradID) || anzahl < 0 || myController.isDayinPast(liefertag)) return null;
        Bestellung neueBestellung = myController.bestellungeinbauen(liefertag, fahrradID, anzahl);
        if (neueBestellung != null) {
            relevanteBestellungen.add(neueBestellung);
        }
        return neueBestellung;
    }

    public Bestellung neueBestellungVersuchen(int fahrradID, int anzahl, String liefertag) {
        return neueBestellungVersuchen(fahrradID, anzahl, myController.getTagIDFromString(liefertag));
    }


    public String getToday() {
        return myController.getToday();
    }


    public void setToday(String heute) {
        myController.setToday(heute);
    }

    public Schichtarbeitstag getSchichtarbeitstag_heute() {
        Schichtarbeitstag relevant = myController.getTagFromID(myController.getToday(0));
        if (relevant != null) {
            relevanteAT.add(relevant);
            return relevant;
        }
        return null;
    }


    public boolean nextLieferung() {
        lieferungenZeiger++;
        if (lieferungenZeiger >= heutigeLieferungen.length) {
            return false;
        }
        return true;
    }

    public void setTagDerLieferungen(String datum) {
        Schichtarbeitstag tag = getTag(datum);
        heutigeLieferungen = tag.getLieferungen().toArray(new Lieferung[0]);
        relevanteLieferungen.addAll(tag.getLieferungen());
        lieferungenZeiger = -1;
    }

    public String[] getCurrentLieferungInfos() {
        String[] infos = new String[LIEFERUNGENINFOLENGTH];
        infos[0] = heutigeLieferungen[lieferungenZeiger].getAnkunftstag_datum();
        infos[1] = Komponentenzuordnung.getCanonicalName(heutigeLieferungen[lieferungenZeiger].getKomponenttyp_ID());

        infos[2] = Integer.toString(heutigeLieferungen[lieferungenZeiger].getAnzahl());
        infos[3] = "no";
        infos[4] = heutigeLieferungen[lieferungenZeiger].getStarttag_datum();
        return infos;
    }


    public String[] getCurrentBestellungInfos() {
        String[] infos = new String[BESTELLUNGENINFOLENGTH];

        infos[0] = heutigeBestellungen[bestellungenzeiger].getBestellungstag();
        infos[1] = heutigeBestellungen[bestellungenzeiger].getLiefertag();
        infos[2] = Komponentenzuordnung.getCanonicalName(heutigeBestellungen[bestellungenzeiger].getModelltyp_ID());

        infos[3] = Integer.toString(heutigeBestellungen[bestellungenzeiger].getAnzahl());
        return infos;
    }


    public void setTagderBestellungen(String datum) {
        Schichtarbeitstag tag = getTag(datum);
        heutigeBestellungen = tag.getBestellungen().toArray(new Bestellung[0]);
        relevanteBestellungen.addAll(tag.getBestellungen());
        bestellungenzeiger = -1;
    }

    public boolean nextBestellung() {
        bestellungenzeiger++;
        if (bestellungenzeiger >= heutigeBestellungen.length) {
            return false;
        }
        return true;
    }

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

    public String[] getLagerbestandTag(int datum) {
        String[] bestand = new String[44];
        Schichtarbeitstag tag = myController.getAlleTage()[datum];
        int offset = 0;
        for (int i = 0; i < 44; i += 2) {
            bestand[i] = Komponentenzuordnung.getCanonicalName(i + 1);
            bestand[i + 1] = Integer.toString(tag.getLager2()[i - offset]);
            if (i != 0) {
                offset++;
            }
        }

        return bestand;
    }

    public String[][] getLagerbestandInRange(String datumErsterTag, String datumLetzterTag) {
        int erstertag = myController.getTagIDFromString(datumErsterTag);
        int letzterTag = myController.getTagIDFromString(datumLetzterTag);
        String[][] returnvalue = new String[(letzterTag - erstertag) + 1][44];
        Schichtarbeitstag tag = myController.getAlleTage()[letzterTag];
        for (int i = returnvalue.length - 1; i >= 0; i--) {
            returnvalue[i] = getLagerbestandTag(tag.getArbeitstag_ID());
            tag = tag.getVortag();
        }
        return returnvalue;
    }


    public int getLieferAnzahl() {
        return heutigeLieferungen.length;
    }

    public int getBestellanzahl() {
        return heutigeBestellungen.length;
    }

    public int getLieferungeninfolength() {
        return LIEFERUNGENINFOLENGTH;
    }

    public int getBestellungeninfolength() {
        return BESTELLUNGENINFOLENGTH;
    }
}
