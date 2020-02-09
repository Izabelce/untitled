package Database_Connectivity_Module;

import Data_Manipulation_Module.Land;

import java.util.List;

public class Zulieferer {
    private int zuliefererID;
    private List<Lieferung> alleLieferungen;
    private final int mindestLiefermenge;
    private Land myLand;
    private int[] komponenten;
    private int rueckstand;

    public Zulieferer(Land land, int[] komponenten, int mlm, int zuliefererID) {
        myLand = land;
        this.komponenten = komponenten;
        mindestLiefermenge = mlm;
        rueckstand = 0;
        this.zuliefererID = zuliefererID;
    }

    public int getNextPossibleLiefertag() {
        return -1;
    }


    public int getZuliefererID() {
        return zuliefererID;
    }

    public void setZuliefererID(int zuliefererID) {
        this.zuliefererID = zuliefererID;
    }

    public List<Lieferung> getAlleLieferungen() {
        return alleLieferungen;
    }

    public void setAlleLieferungen(List<Lieferung> alleLieferungen) {
        this.alleLieferungen = alleLieferungen;
    }

    public int getMindestLiefermenge() {
        return mindestLiefermenge;
    }

    public Land getMyLand() {
        return myLand;
    }

    public void setMyLand(Land myLand) {
        this.myLand = myLand;
    }

    public int[] getKomponenten() {
        return komponenten;
    }

    public void setKomponenten(int[] komponenten) {
        this.komponenten = komponenten;
    }

    public int getRueckstand() {
        return rueckstand;
    }

    public void setRueckstand(int rueckstand) {
        this.rueckstand = rueckstand;
    }


}
