package sample;

import Data_Manipulation_Module.Data_Manipulation_Interface;
import Database_Connectivity_Module.Bestellung;

public class Backend_Interface {
    private Data_Manipulation_Interface dataInterface;
    private static Backend_Interface single_instance = null;

    private Backend_Interface(Data_Manipulation_Interface data_interface) {
        this.dataInterface = data_interface;
    }

    public String getHeute() {
        return dataInterface.getToday();
    }

    //TO-DO Methode set Heute implementieren
    public void setHeute(String heute) {
        dataInterface.setToday(heute);
    }

    public Bestellung neueBestellungAnlegen(int fahrradID, String wunschliefertermin, int anzahl) {
        return (dataInterface.neueBestellungVersuchen(fahrradID, anzahl, wunschliefertermin));
    }

    public static Backend_Interface getInstance(Data_Manipulation_Interface dataInterface) {
        if (dataInterface != null && single_instance == null) {
            single_instance = new Backend_Interface(dataInterface);
        }
        return single_instance;
    }

    public String countLieferungen() {
        return Integer.toString(dataInterface.getSchichtarbeitstag_heute().getLieferungen().size());
    }

    public String countBestellungen() {
        return Integer.toString(dataInterface.getSchichtarbeitstag_heute().getBestellungen().size());
    }

    public String getSchicht() {
        return Integer.toString(dataInterface.getSchichtarbeitstag_heute().getSchicht_ID());
    }

    public String getProdvol() {
        return Integer.toString(dataInterface.getSchichtarbeitstag_heute().getsum());
    }

    /**
     * gibt ein array über alle infos der Lieferungen des angegebenen tages durch
     *aufbau ist bisher:
     * 0: tag an dem die Lieferung eintrifft
     * 1: ID der komponente (später hier der name...)
     * 2: die anzahl der gelieferten komponenten
     * 3: ob die lieferung schon eingetroffen ist (zur zeit "no")
     * 4: der tag, an dem die lieferung beim Lieferanten erfasst wurde
     * @param datum der tag, von dem die infos kommen
     * @return zweidimensionales array, erste dimension ist die lieferung, zweite dimension sind die Infos der Lieferung
     */
    public String[][] getLieferwerte(String datum) {
        dataInterface.setTagDerLieferungen(datum);
        int anzahl_lieferungen = dataInterface.getLieferAnzahl();
        if (anzahl_lieferungen == 0) {
            return null;
        }
        String[][] infos = new String[anzahl_lieferungen][dataInterface.getInfolenght()];
        int counter = 0;

        while (dataInterface.nextLieferung()) {
            String[] infoelement = dataInterface.getCurrentLieferungInfos();
            for (int i = 0; i < dataInterface.getInfolenght(); i++) {
                infos[counter][i] = infoelement[i];
            }
            counter++;
        }


        return infos;
    }
}




