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
     * aufbau ist bisher:
     * 0: tag an dem die Lieferung eintrifft
     * 1: ID der komponente (später hier der name...)
     * 2: die anzahl der gelieferten komponenten
     * 3: ob die lieferung schon eingetroffen ist (zur zeit "no")
     * 4: der tag, an dem die lieferung beim Lieferanten erfasst wurde
     *
     * @param datum der tag, von dem die infos kommen
     * @return zweidimensionales array, erste dimension ist die lieferung, zweite dimension sind die Infos der Lieferung
     */
    public String[][] getLieferwerte(String datum) {
        dataInterface.setTagDerLieferungen(datum);
        int anzahl_lieferungen = dataInterface.getLieferAnzahl();
        if (anzahl_lieferungen == 0) {
            return null;
        }
        String[][] infos = new String[anzahl_lieferungen][dataInterface.getLieferungeninfolength()];
        int counter = 0;

        while (dataInterface.nextLieferung()) {
            String[] infoelement = dataInterface.getCurrentLieferungInfos();
            for (int i = 0; i < dataInterface.getLieferungeninfolength(); i++) {
                infos[counter][i] = infoelement[i];
            }
            counter++;
        }
        return infos;
    }


    /**
     * gibt ein array über alle infos der Bestellungen des angegebenen tages durch
     * aufbau ist bisher:
     * 0: der Tag an dem bestellt wurde
     * 1: der Tag an dem die Bestellung "abgeholt" wird
     * 2: das Modell des Fahrrads
     * 3: die Anzahl der bestellten Fahrräder
     *
     * @param datum der tag, von dem die infos kommen
     * @return zweidimensionales array, erste dimension ist die Bestellung, zweite dimension sind die Infos der Bestellung
     */
    public String[][] getBestellwerte(String datum) {
        dataInterface.setTagderBestellungen(datum);
        int anzahl_bestellungen = dataInterface.getBestellanzahl();
        if (anzahl_bestellungen == 0) {
            return null;
        }
        String[][] infos = new String[anzahl_bestellungen][dataInterface.getBestellungeninfolength()];
        int counter = 0;

        while (dataInterface.nextBestellung()) {
            String[] infoelement = dataInterface.getCurrentBestellungInfos();
            for (int i = 0; i < dataInterface.getBestellungeninfolength(); i++) {
                infos[counter][i] = infoelement[i];
            }
            counter++;
        }
        return infos;
    }

    public String[][] bestandsreport(String datumErsterTag, String datumLetzterTag) {
        return dataInterface.getLagerbestandInRange(datumErsterTag, datumLetzterTag);
    }

    /**
     * Gibt eine Zahl als String zurück, die die Summe aller am angegebenen Tag zur Produktion eingeplanten Fahrräder ist (also Fahrradtyp 1 bis 8 summiert)
     *
     * @param datum der tag an dem die Summe erhalten werden soll
     * @return summe als String
     */
    String getGesamtSummeProduktionTag(String datum) {
        return dataInterface.getGesamtzahlProduktionDay(datum);
    }

    /**
     * Gibt eine Zahl als String zurück, die die Summe aller geplanten Fahrräder in der Woche sind, zu der der tag gehört (fahrradtyp 1 bis summiert, wenn z.B. datum ein Dienstag ist,
     * werden montag bis sonntag der selben woche summiert)
     *
     * @param datum der tag, von dem die woche summiert werden soll
     * @return summe als string
     */
    String getGesamtSummeProduktionWoche(String datum) {
        return dataInterface.getGesamtzahlProduktionWeek(datum);
    }

    /**
     * gibt eine Zahl als String zurück, die die Summe aller geplanten Fahrräder in dem Monat sind, zu dem der Tag gehört (fahrradtyp 1 bis 8 summiert, ist datum z.b. 11.02.2021, wird 01.02.2021 bis 28.02.2021 summiert)
     *
     * @param datum der Tag, von dem der Monat summiert werden soll
     * @return summe als String
     */
    String getGesamtSummeProduktionMonat(String datum) {
        return dataInterface.getGesamtzahlProduktionMonth(datum);
    }

    /**
     * gibt die gesamtzahl Produktion in 2021 zurück, das Datum wird ignoriert
     *
     * @param datum egal
     * @return Die Summe als String
     */
    String getGesamtSummeProduktionJahr(String datum) {
        return dataInterface.getGesamtProduktionJahr();
    }

    /**
     * Gibt ein 2-d array String[i][j] über strings zurück
     * aber nur der erste eintrag der ersten dimension [i] ist relevant
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen einträge stehen für die jeweiligen komponenten
     *
     * @param datum das datum, an dem die bedarfe abgefragt werden
     * @return s. beschreibung
     */
    String[][] getsekundarBedarfTag(String datum) {
        return dataInterface.getSekundarBedarfTag(datum);
    }

    /**
     * Gibt ein 2-d array String[i][j] über Strings zurück
     * Die daten sind alle tage der woche, in der sich der angegebene tag befindet
     * die dimension [i] steht für jeweils einen tag
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen einträge stehen für die jeweiligen komponenten
     * der letzte eintrag der Dimension[i] beschreibt die summe aller bedarfe
     *
     * @param datum der tag, von dem die woche abgefragt wird
     * @return s. beschreibung
     */
    String[][] getSekundarBedarfWoche(String datum) {
        return dataInterface.getSekundarbedarfWoche(datum);
    }

    /**
     * Gibt ein 2-d array String[i][j] über Strings zurück
     * Die daten sind alle tage des monats, in der sich der angegebene tag befindet
     * die dimension [i] steht für jeweils einen tag
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen einträge stehen für die jeweiligen komponenten
     * der letzte eintrag der Dimension[i] beschreibt die summe aller bedarfe
     *
     * @param datum der tag, von dem der Monat abgefragt wird
     * @return s. beschreibung
     */
    String[][] getSukndarBedarfMonat(String datum) {
        return dataInterface.getSekundarbedarfMonat(datum);
    }

    /**
     * Gibt ein 2-d array String[i][j] über Strings zurück
     * Die daten sind alle tage des jahres 2021
     * die dimension [i] steht für jeweils einen tag
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen
     * der letzte eintrag der Dimension[i] beschreibt die summe aller bedarfe
     * @param datum egal kann auch null sein
     * @return s. beschreibung
     */
    String[][] getSekundarBedarfJahr(String datum){
        return dataInterface.getSekundarbedarfeJahr();
    }


    /**
     * Gibt 2-d array String[i][j] über Strings zurück
     * die daten sind alle tage des jahres 2021
     * die dimension [i] steht für jeweils einen tag
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen sind die fahrräder
     * die Länge ist also insgesamt 9 (j =0 ist datum, j =1 bis J=8 sind fahrräder)
     *
     * @param datum egal, kann auch null sein
     * @return s.beschreibung
     */
    public String[][] produktionsplanungJahr(String datum) {

        return dataInterface.getProduktionsplanungJahr(datum);
    }

    /**
     * gibt 2-d array String[i][j] über Strings zurück
     * die daten sind alle tage des monats, zu dem der tag (datum) gehört
     * die dimension [i] steht für jeweils einen tag
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen sind die fahrräder
     * die Länge ist also insgesamt 9 (j =0 ist datum, j =1 bis J=8 sind fahrräder)
     * @param datum tag, von dem der monat angezeigt werden soll
     * @return s. beschreibung
     */
    public String[][] produktionsplanungMonat(String datum) {

        return dataInterface.getProduktionsplanungMonat(datum);
    }

    /**
     * gibt 2-d array String[i][j] über Strings zurück
     * die daten sind alle tage der woche, zu dem der tag (datum) gehört
     * die dimension [i] steht für jeweils einen tag
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen sind die fahrräder
     * die Länge ist also insgesamt 9 (j =0 ist datum, j =1 bis J=8 sind fahrräder)
     * @param datum tag, von dem die woche angezeigt werden soll
     * @return s. beschreibung
     */
    public String[][] produktionsplanungWoche(String datum) {

        return  dataInterface.getProduktionsplanungWoche(datum);
    }

    /**
     * gibt 2-d array String[i][j] über Strings zurück
     * die daten sind der tag, der im datum angegeben ist
     * die dimension [i] steht für jeweils einen tag und hat somit nur 1 stelle (index =0)
     * die dimension[j] steht für die bedarfe an dem tag[i]
     * für die daten der dimension 2, [j] gilt: erster Eintrag ist das Datum des tages, alle anderen sind die fahrräder
     * die Länge ist also insgesamt 9 (j =0 ist datum, j =1 bis J=8 sind fahrräder)
     * @param datum
     * @return
     */
    public String[][] produktionsplanungTag(String datum) {

        return dataInterface.getProduktionsplanungTag(datum);
    }


}




