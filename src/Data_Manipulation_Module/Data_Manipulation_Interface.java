package Data_Manipulation_Module;

import Database_Connectivity_Module.Bestellung;

public class Data_Manipulation_Interface {
    private Data_Manipulation_Controller myController;

    public Data_Manipulation_Interface() {

    }

    public void enlistController(Data_Manipulation_Controller con) {
        myController = con;
    }

    public Bestellung neueBestellungVersuchen(int fahrradID, int anzahl, int liefertag) {
        if(Plausibility.isWrongID(fahrradID) || anzahl < 0|| myController.isDayinPast(liefertag))return null;
        Bestellung neueBestellung = new Bestellung(myController.getToday(0), liefertag, fahrradID, anzahl);
        if(myController.bestellungeinbauen(neueBestellung))return neueBestellung;
        return null;
    }
    public Bestellung neueBestellungVersuchen(int fahrradID, int anzahl, String liefertag){
     return neueBestellungVersuchen(fahrradID, anzahl, myController.getTagIDFromString(liefertag));
    }

    public void calculateDayZielvorgaben() {
        myController.zielVorgabenWorkflow();
    }

    public String getToday() {
        return myController.getToday();
    }

    public void test(){
        System.out.println(neueBestellungVersuchen(2, 33, 129));
        System.out.println(neueBestellungVersuchen(2, 33, 123));
    }

    public void setToday(String heute) {
        myController.setToday(heute);
    }
}
