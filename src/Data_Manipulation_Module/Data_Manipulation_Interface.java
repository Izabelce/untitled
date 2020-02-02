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
        Bestellung neueBestellung = new Bestellung(myController.getToday(0), liefertag, fahrradID, anzahl);
        if (myController.bestellungeinbauen(neueBestellung)){
            relevanteBestellungen.add(neueBestellung);
            return neueBestellung;
        }
        return null;
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
        Schichtarbeitstag relevant =  myController.getTagFromID(myController.getToday(0));
        if(relevant != null){
            relevanteAT.add(relevant);
            return relevant;
        }
        return null;
    }


}
