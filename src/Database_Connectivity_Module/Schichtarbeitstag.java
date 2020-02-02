package Database_Connectivity_Module;

import Data_Manipulation_Module.Komponentenzuordnung;
import Data_Manipulation_Module.Land;

import java.util.*;

public class Schichtarbeitstag {
    private int schicht_ID;
    private int arbeitstag_ID;
    private int max_output;
    int[] fahrradplan;
    private int kw_id;
    private int sum;
    private int monats_ID;
    private final String datum;
    private Map<Integer, Integer> lager;
    private HashSet<Data_Manipulation_Module.Land> holidayIn;
    private List<Lieferung> heutigeLieferungen;
    private Schichtarbeitstag vortag;
    private List<Bestellung> heutigeBestellungen;

    public Schichtarbeitstag(int schicht_ID, int max_output, int[] arbeitsmappe, int kw_id, int tag_ID, HashSet<Land> holidays, String datum) {
        this.vortag = null;
        this.schicht_ID = schicht_ID;
        this.max_output = max_output;
        this.arbeitstag_ID = tag_ID;
        this.datum = datum;
        fahrradplan = arbeitsmappe;
        this.kw_id = kw_id;
        this.sum = 0;
        this.holidayIn = holidays;
        heutigeLieferungen = new LinkedList<Lieferung>();
        heutigeBestellungen = new LinkedList<Bestellung>();
        lager = new HashMap<Integer, Integer>();
        monats_ID = 0;
        for (int i = 1; i <= 22; i++) {
            lager.put(i, 0);
        }
        for (int i = 0; i < fahrradplan.length; i++) {
            sum = sum + fahrradplan[i];
        }
    }

    public void setVortag(Schichtarbeitstag vortag) {
        if (vortag != null) {
            if (vortag.getArbeitstag_ID() != this.getArbeitstag_ID() - 1)
                throw new IllegalArgumentException("ILLEGAL Vorherigerarbeitstag");
        }
        this.vortag = vortag;
    }

    public Schichtarbeitstag getVortag() {
        return this.vortag;
    }

    public boolean isHoldayIn(Data_Manipulation_Module.Land candidate) {
        return holidayIn.contains(candidate);
    }

    public void setMonats_ID(int monats_ID) {
        this.monats_ID = monats_ID;
    }

    public void lagerbestandBerechnen(Schichtarbeitstag vortag) {
        //wenn vortag null, dann ist das wohl der erste tag
        if (vortag != null) {
            for (Integer key : lager.keySet()) {
                lager.put(key, vortag.getLagerbestand(key));
            }

            for (int i = 0; i < fahrradplan.length; i++) {
                int anzahl = fahrradplan[i];
                int gabelId = Komponentenzuordnung.getGabelID(i + 1);
                int sattelId = Komponentenzuordnung.getSattelID(i + 1);
                int rahmenId = Komponentenzuordnung.getRahmenID(i + 1);

                lager.put(gabelId, lager.get(gabelId) - anzahl);
                lager.put(sattelId, lager.get(sattelId) - anzahl);
                lager.put(rahmenId, lager.get(rahmenId) - anzahl);
            }

            for (Lieferung l : vortag.heutigeLieferungen) {
                lager.put(l.getKomponenttyp_ID(), lager.get(l.getKomponenttyp_ID()) + l.getAnzahl());
            }

            for (int i = 0; i < 8; i++) {
                lager.put(i + 1, lager.get(i + 1) + vortag.fahrradplan[i] - vortag.getBestellmenge(i+1) );
            }
        }
    }

    private int getBestellmenge(int i) {
        int returnvalue =0;
        for(Bestellung b: heutigeBestellungen){
            if(b.getModelltyp_ID() == i){
                returnvalue += b.getAnzahl();
            }
        }
        return returnvalue;

    }

    public boolean isNegativeLager() {
        boolean boo = false;
        for (Integer i : lager.keySet()) {
            if (lager.get(i) < 0) boo = true;
        }
        return boo;
    }

    public int getNeededAmount(int komponentID) {
        if (lager.get(komponentID) < 0) return (-1)*lager.get(komponentID);
        return 0;
    }

    public int getSchicht_ID() {
        return schicht_ID;
    }

    public void setSchicht_ID(int schicht_ID) {
        this.schicht_ID = schicht_ID;
    }

    public int getMax_output() {
        return max_output;
    }

    public void setMax_output(int max_output) {
        this.max_output = max_output;
    }

    public boolean hasDeficit() {
        return (sum > max_output);
    }

    public int getDeficitAmount(int fahrrad_Id) {
        return sum - max_output;
    }

    public int extractDeficit(int fahrrad_id) {
        int returnvalue = 0;
        if (hasDeficit()) {
            returnvalue = (int) ((double) fahrradplan[fahrrad_id] * getAnteil(fahrrad_id));
            sum = sum - returnvalue;
            if (returnvalue > fahrradplan[fahrrad_id]) {
                throw new IllegalStateException(String.format("Zu viel defizit berechnet! Tag: %d, defi: %d, lager: %d, teil: %d", this.arbeitstag_ID, returnvalue, fahrradplan[fahrrad_id], fahrrad_id));
            }
            fahrradplan[fahrrad_id] = (fahrradplan[fahrrad_id] - returnvalue);
        }
        return returnvalue;
    }

    private double getAnteil(int fahrrad_id) {
        double firstpart = (1d / (double) sum);
        double secondpart = ((double) fahrradplan[fahrrad_id] * firstpart);
        return secondpart;
    }

    public int getKwID() {
        return kw_id;
    }

    public int getsum() {
        return sum;
    }

    /**
     * map von fahrrad id und gesammelte anzahl aus extractdeficit methoden der späteren wochen
     * wird dann auf den tag verteilt.
     *
     * @param summedDeficits
     */
    public int[] addWeeksDeficits(int[] summedDeficits, int limit) {
        if (this.holidayIn.contains(Land.Nordrhein_Westfalen)) {
            max_output = 0;
        } else {
            this.max_output = limit;
            for (int i = 0; i < summedDeficits.length; i++) {
                int old = fahrradplan[i];
                int amount = summedDeficits[i];
                int maxInput = getmaxInput(amount);
                fahrradplan[i] = old + maxInput;
                sum = sum + maxInput;
                summedDeficits[i] = summedDeficits[i] - maxInput;
                if (summedDeficits[i] < 0) {
                    throw new IllegalStateException(String.format("Zu viel defizit herausgenommen! Tag: %d, defi: %d, summeddefi: %d, teil: %d", this.arbeitstag_ID, maxInput, summedDeficits[i], i));
                }
            }
        }
        return summedDeficits;
    }

    private int getmaxInput(int amount) {
        int possible_amount = max_output - sum;
        if (amount > possible_amount) {
            return possible_amount;
        } else {
            return amount;
        }
    }

    public int getLagerbestand(int lagerobjektID) {
        return lager.get(lagerobjektID);
    }

    public int getFahrradBestand(int fahrrad_ID) {
        if (fahrrad_ID > 10 || fahrrad_ID > 22)
            throw new IllegalArgumentException("Es gibt kein Fahrrad mit dieser ID");
        return lager.get(fahrrad_ID);
    }

    public int getPartBestand(int partID) {
        if (partID < 0 || partID > 9) throw new IllegalArgumentException("Es gibt kein Fahrradteil mit dieser ID");
        return lager.get(partID);
    }


    public int[] getVerteilung() {
        return this.fahrradplan;
    }

    public int getArbeitstag_ID() {
        return this.arbeitstag_ID;
    }

    public void limitAnpassen(int limit) throws IllegalArgumentException {
        int mult = 1;
        if (!holidayIn.contains(Land.Nordrhein_Westfalen)) {
            while (sum > limit * mult) {
                mult++;
            }
            this.max_output = mult * limit;
            this.schicht_ID = mult;
        } else {
            mult = 1;
            this.max_output = 0;
            this.schicht_ID = 0;
        }
        boolean isWorkday = false;
        for (int f : fahrradplan) {
            if (f != 0) isWorkday = true;
        }
        if (isWorkday && holidayIn.contains(Land.Nordrhein_Westfalen)) {
            throw new IllegalArgumentException(String.format("Ist FEIERTAG, aber soll als Tag mit %d Schichten gelten!:", mult));
        }
        if (max_output > 1560)
            throw new IllegalArgumentException(String.format("maxoutput limit überschritten! kann nur 1560, ist aber %d", mult * limit));
    }
    //public void schichtenberechnen(int lim){
    //  if(sum > lim){
    //     schicht_ID =(int) Math.ceil((double) sum / lim);
    //}
    //if(schicht_ID >= 4)System.err.println("OH FUCK ES GIBT MEHR ZU TUN ALS MÖGLICH IST");
    //}

    public void addVerbrauch(int fahrradID, int anzahl) {
        this.fahrradplan[fahrradID - 1] = fahrradplan[fahrradID - 1] + anzahl;
        sum = sum + anzahl;
    }

    public void addLieferung(Lieferung eingehende) {
        this.heutigeLieferungen.add(eingehende);
    }

    public void setLagerbestand(int part_ID, int anzahl) {
        lager.put(part_ID, anzahl);
    }

    public List<Lieferung> getLieferungen() {
        return this.heutigeLieferungen;
    }
    public void addBestellung(Bestellung b){
        heutigeBestellungen.add(b);
    }

    public List<Bestellung> getBestellungen(){
        return this.heutigeBestellungen;
    }



    public void checkMyself() {

        for (int c = 0; c < fahrradplan.length; c++) {
            if (fahrradplan[c] < 0) {
                throw new IllegalStateException(String.format("Fahrradplan unter null! Tag_ID: %d, Anzahl: %d", this.arbeitstag_ID, fahrradplan[c]));
            }
        }

        for (Integer key : lager.keySet()) {
            if (lager.get(key) < 0) {
                throw new IllegalStateException(String.format("Lager unter null! Tag_ID: %d, Anzahl: %d, Teil: %d ", this.arbeitstag_ID, lager.get(key), key));
            }
        }

        for (Lieferung l : heutigeLieferungen) {
            if (l.getAnzahl() < 0) {
                throw new IllegalStateException(String.format("Lieferung unter null! Tag_ID: %d, Anzahl: %d, Teil: %d",arbeitstag_ID, l.getAnzahl(), l.getKomponenttyp_ID()));
            }
        }
    }

    public Schichtarbeitstag getCopy() {
        int[] fahrradplanCopy = new int[fahrradplan.length];
        for(int i=0; i< fahrradplan.length; i++){
            fahrradplanCopy[i] = fahrradplan[i];
        }
        HashSet<Land> holidayCopy = new HashSet<Land>();
        for(Land l : holidayIn){
            holidayCopy.add(l);
        }
        Schichtarbeitstag returnTag = new Schichtarbeitstag(this.schicht_ID, this.max_output, fahrradplanCopy, this.kw_id, this.arbeitstag_ID, holidayCopy, this.datum);
        for(Bestellung b: heutigeBestellungen){
            returnTag.addBestellung(b.getCopy());
        }
        for(Lieferung l : heutigeLieferungen){
            returnTag.addLieferung(l.getCopy());
        }

        returnTag.setMonats_ID(this.monats_ID);
        for(Integer key : lager.keySet()){
            returnTag.setLagerbestand(key, lager.get(key));
        }
        returnTag.sum = this.sum;

        return returnTag;
    }


    public String getDatum(){
        return this.datum;
    }
}
