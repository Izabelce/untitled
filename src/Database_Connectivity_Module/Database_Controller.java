package Database_Connectivity_Module;


import java.util.LinkedList;
import java.util.List;

public class Database_Controller {
    private Database_Connector dbCon;
    private Database_Helper dbHelp;
    private Database_Connectivity_Interface dbInterface;
    private String heute;
    private int heuteId;

    private List<Bestellung> allBestellung;

    public Database_Controller(Database_Connector dbCon, Database_Helper dbHelp) {
        this(dbCon, dbHelp, "01.10.2020");
    }

    public Database_Controller(Database_Connector dbCon, Database_Helper dbHelp, String heute) {
        this.dbCon = dbCon;
        this.dbHelp = dbHelp;
        dbInterface = new Database_Connectivity_Interface(dbCon,this);
        dbCon.assignHelper(dbHelp);
        dbCon.assignController(this);

        this.heute = heute;
        heuteId = dbCon.getKalendertagID(heute);
        allBestellung = new LinkedList<Bestellung>();
    }



    private Bestellung hasIssuedBestellung(int bestellID){
        for(Bestellung b: allBestellung){
            if(b.getBestell_ID() == bestellID) return b;
        }
        return null;
    }

    public Bestellung  getBestellungOnID(int bestellID){
        Bestellung b = hasIssuedBestellung(bestellID);
        if ( b == null){
            // dbCon.
        }

        return b;
    }

    public String getToday(){
        return heute;
    }

    public int getTodayID(){return heuteId;}

    public Database_Connectivity_Interface getDbInterface() {
        return dbInterface;
    }


    public void commit() {
        dbCon.commit();
    }
}
