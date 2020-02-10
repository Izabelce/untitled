package sample;

import javafx.beans.property.SimpleStringProperty;

public class LieferungAnzeigeHelfer {

    private final SimpleStringProperty ankunftstag;
    private final SimpleStringProperty komponente;
    private final SimpleStringProperty anzahl;
    private final SimpleStringProperty istEingetroffen;
    private final SimpleStringProperty erfassungstag;

    public LieferungAnzeigeHelfer(String ankunftstag, String komponente, String anzahl, String isteingetroffen, String erfassungstag) {
        this.ankunftstag = new SimpleStringProperty(ankunftstag);
        this.komponente = new SimpleStringProperty(komponente);
        this.anzahl = new SimpleStringProperty(anzahl);
        this.istEingetroffen = new SimpleStringProperty(isteingetroffen);
        this.erfassungstag = new SimpleStringProperty(erfassungstag);
    }

    public String getAnkunftstag() {
        return ankunftstag.get();
    }

    public SimpleStringProperty ankunftstagProperty() {
        return ankunftstag;
    }

    public String getKomponente() {
        return komponente.get();
    }

    public SimpleStringProperty komponenteProperty() {
        return komponente;
    }

    public String getAnzahl() {
        return anzahl.get();
    }

    public SimpleStringProperty anzahlProperty() {
        return anzahl;
    }

    public String getIstEingetroffen() {
        return istEingetroffen.get();
    }

    public SimpleStringProperty istEingetroffenProperty() {
        return istEingetroffen;
    }

    public String getErfassungstag() {
        return erfassungstag.get();
    }

    public SimpleStringProperty erfassungstagProperty() {
        return erfassungstag;
    }

}
