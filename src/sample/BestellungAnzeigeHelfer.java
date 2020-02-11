package sample;

import javafx.beans.property.SimpleStringProperty;

public class BestellungAnzeigeHelfer {

    private final SimpleStringProperty bestellungstag;
    private final SimpleStringProperty abholungstag;
    private final SimpleStringProperty anzahl;
    private final SimpleStringProperty modell;

    public BestellungAnzeigeHelfer(String bestellungstag, String abholungstag, String anzahl, String modell) {
        this.bestellungstag = new SimpleStringProperty(bestellungstag);
        this.abholungstag = new SimpleStringProperty(abholungstag);
        this.anzahl = new SimpleStringProperty(anzahl);
        this.modell = new SimpleStringProperty(modell);
    }

    public String getBestellungstag() {
        return bestellungstag.get();
    }

    public SimpleStringProperty bestellungstagProperty() {
        return bestellungstag;
    }

    public void setBestellungstag(String bestellungstag) {
        this.bestellungstag.set(bestellungstag);
    }

    public String getAbholungstag() {
        return abholungstag.get();
    }

    public SimpleStringProperty abholungstagProperty() {
        return abholungstag;
    }

    public void setAbholungstag(String abholungstag) {
        this.abholungstag.set(abholungstag);
    }

    public String getAnzahl() {
        return anzahl.get();
    }

    public SimpleStringProperty anzahlProperty() {
        return anzahl;
    }

    public void setAnzahl(String anzahl) {
        this.anzahl.set(anzahl);
    }

    public String getModell() {
        return modell.get();
    }

    public SimpleStringProperty modellProperty() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell.set(modell);
    }




}
