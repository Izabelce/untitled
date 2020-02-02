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
    private static final int INFOLENGHT =5;
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

    public void calculateDayZielvorgaben() {
        myController.zielVorgabenWorkflow();
    }

    public String getToday() {
        return myController.getToday();
    }

    public void test() {
        System.out.println(neueBestellungVersuchen(2, 33, 129));
        System.out.println(neueBestellungVersuchen(2, 33, 123));
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
        heutigeLieferungen = tag.getLieferungen().toArray(heutigeLieferungen);
        relevanteLieferungen.addAll(tag.getLieferungen());
        lieferungenZeiger = -1;
    }

    public String[] getCurrentLieferungInfos() {
        String[] infos = new String[INFOLENGHT];
        infos[0] = heutigeLieferungen[lieferungenZeiger].getAnkunftstag_datum();
        infos[1] = Integer.toString(heutigeLieferungen[lieferungenZeiger].getKomponenttyp_ID());
        //TODO Methode einbauen die den richtigen namen der komponente mappt
        infos[2] = Integer.toString(heutigeLieferungen[lieferungenZeiger].getAnzahl());
        infos[3] = "no";
        infos[4] = heutigeLieferungen[lieferungenZeiger].getStarttag_datum();

        return infos;
    }


    public int getLieferAnzahl() {
        return heutigeLieferungen.length;
    }

    public int getInfolenght(){
        return  INFOLENGHT;
    }
}
