package Database_Connectivity_Module;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Kalenderwoche {
    private Kalenderwoche vorwoche;
    private Schichtarbeitstag[] arbeitstageDerKW;
    private int pointer;

    public static List<Kalenderwoche> getKWListFromTage(List<Schichtarbeitstag> alleTage) {
        HashMap<Integer, Kalenderwoche> mapping = new HashMap<Integer, Kalenderwoche>();
        List<Kalenderwoche> kwWeek = new LinkedList<Kalenderwoche>();
        for (Schichtarbeitstag schichtarbeitstag : alleTage) {
            Integer kwID = schichtarbeitstag.getKwID();
            if (mapping.get(kwID) == null) {
                mapping.put(kwID, new Kalenderwoche(kwID));
            }
            mapping.get(kwID).add(schichtarbeitstag);
        }
        for (Integer i : mapping.keySet()) {
            Kalenderwoche k = mapping.get(i);
            k.setVorwoche(mapping.get(i-1));
            k.reset();
            kwWeek.add(k);
        }
        return kwWeek;
    }

    public int getKwID() {
        return kwID;
    }

    private int kwID;

    public Kalenderwoche(int kwID) {
        arbeitstageDerKW = new Schichtarbeitstag[0];
        pointer = -1;
        this.kwID = kwID;
    }

    public void add(Schichtarbeitstag s) throws IllegalArgumentException {
        if (s.getKwID() != kwID)
            throw new IllegalArgumentException(String.format("KalenderID des Inputtages(%d) stimmt nicht mit der Woche(%d) überein!", s.getKwID(), kwID));
        next();
        if (pointer == arbeitstageDerKW.length) {
            resize();
        }
        arbeitstageDerKW[pointer] = s;
    }

    public Schichtarbeitstag get() {
        if (pointer == -1) {
            return null;
        } else if (pointer >= arbeitstageDerKW.length) {
            return null;
        }
        return arbeitstageDerKW[pointer];
    }

    public boolean next() {
        pointer = pointer + 1;
        return pointer < arbeitstageDerKW.length;
    }

    private void resize() {
        Schichtarbeitstag[] bigger = new Schichtarbeitstag[arbeitstageDerKW.length + 1];
        for (int i = 0; i < arbeitstageDerKW.length; i++) {
            bigger[i] = arbeitstageDerKW[i];
        }
        arbeitstageDerKW = bigger;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return false;
        if (o instanceof Kalenderwoche) {
            Kalenderwoche k = (Kalenderwoche) o;
            return k.kwID == this.kwID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        Integer i = kwID;
        return i.hashCode();
    }

    public void merge(Kalenderwoche k2) {
        if (!this.equals(k2)) throw new IllegalArgumentException("falsche KW");
        while (k2.next()) {
            this.add(k2.get());
        }
        this.reset();
    }

    public void reset() {
        pointer = -1;
    }

    public Schichtarbeitstag getLast() {
        return this.arbeitstageDerKW[arbeitstageDerKW.length - 1];
    }

    public Schichtarbeitstag getFirst() {
        return this.arbeitstageDerKW[0];
    }

    public void setVorwoche(Kalenderwoche vorwoche){
        this.vorwoche = vorwoche;
    }

    public Kalenderwoche getVorwoche() {
        return vorwoche;
    }
}
