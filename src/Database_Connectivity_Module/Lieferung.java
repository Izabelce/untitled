package Database_Connectivity_Module;

public class Lieferung {
    private int komponenttyp_ID;
    private int anzahl;
    private int ankunftstag_ID;
    private int starttag_ID;

    /**
     * Kompenent_ID von 9 bis 22
     *
     * @param komponenttyp_ID
     * @param anzahl
     * @param ankunftstag_ID
     * @param starttag_ID
     */
    public Lieferung(int komponenttyp_ID, int anzahl, int ankunftstag_ID, int starttag_ID) {
        this.komponenttyp_ID = komponenttyp_ID;
        this.anzahl = anzahl;
        this.ankunftstag_ID = ankunftstag_ID;
        this.starttag_ID = starttag_ID;

    }

    public int getAnzahl() {
        return anzahl;
    }

    public int getAnkunftstag_ID() {
        return ankunftstag_ID;
    }

    public int getStarttag_ID() {
        return starttag_ID;
    }


    public int getKomponenttyp_ID() {
        return komponenttyp_ID;
    }

    public Lieferung getCopy() {
        return new Lieferung(this.komponenttyp_ID, this.anzahl, this.ankunftstag_ID, this.starttag_ID);
    }
}
