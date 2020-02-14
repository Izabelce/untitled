package sample;

import javafx.beans.property.SimpleStringProperty;

public class Zielmarkte {

    private SimpleStringProperty land;
    private SimpleStringProperty anteil;




    public Zielmarkte(String land, String anteil) {
        this.land = new SimpleStringProperty(land);
        this.anteil = new SimpleStringProperty(anteil);
    }

    public String getLand() {
        return land.get();
    }

    public SimpleStringProperty landProperty() {
        return land;
    }

    public void setLand(String land) {
        this.land.set(land);
    }

    public String getAnteil() {
        return anteil.get();
    }

    public SimpleStringProperty anteilProperty() {
        return anteil;
    }

    public void setAnteil(String anteil) {
        this.anteil.set(anteil);
    }
}
