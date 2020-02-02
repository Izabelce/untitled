package Database_Connectivity_Module;
/*
ist eine fusion der zwei tables bestellung und bestellt, weil das sinn macht
 */
public class Bestellung {

    private int bestell_ID;
    private String bestellungstag;
    private String liefertag;
    private int bestelldatum_ID;
    private int lieferdatum_ID;
    private int modelltyp_ID;
    private int anzahl;

    public Bestellung(int bestelldatum_ID, int lieferdatum_ID, int modelltyp_ID, int anzahl, String bestelldatum, String lieferdatum) {
        this.bestelldatum_ID = bestelldatum_ID;
        this.lieferdatum_ID = lieferdatum_ID;
        this.modelltyp_ID = modelltyp_ID;
        this.anzahl = anzahl;
        this.bestellungstag = bestelldatum;
        this.liefertag = lieferdatum;

    }
    public Bestellung getCopy(){
        return  new Bestellung(this.bestelldatum_ID, this.lieferdatum_ID, this.modelltyp_ID, this.anzahl, this.bestellungstag, this.liefertag);
    }

    public int getBestell_ID() {
        return bestell_ID;
    }

    public int getBestelldatum_ID() {
        return bestelldatum_ID;
    }

    public void setBestelldatum_ID(int bestelldatum_ID) {
        this.bestelldatum_ID = bestelldatum_ID;
    }

    public int getLieferdatum_ID() {
        return lieferdatum_ID;
    }

    public void setLieferdatum_ID(int lieferdatum_ID) {
        this.lieferdatum_ID = lieferdatum_ID;
    }

    public int getModelltyp_ID() {
        return modelltyp_ID;
    }

    public void setModelltyp_ID(int modelltyp_ID) {
        this.modelltyp_ID = modelltyp_ID;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public void setBestell_ID(int bestell_ID) {
        this.bestell_ID = bestell_ID;
    }
}
