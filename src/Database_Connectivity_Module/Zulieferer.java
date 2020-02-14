package Database_Connectivity_Module;

import Data_Manipulation_Module.Data_Manipulation_Controller;
import Data_Manipulation_Module.Land;

import java.util.LinkedList;
import java.util.List;

public class Zulieferer {
    private int zuliefererID;
    private List<Lieferung> alleLieferungen;
    private final int mindestLiefermenge;
    private Land myLand;
    private int[] komponenten;
    private int[] rueckstand;
    private final TagArt[] tagArts;
    private Data_Manipulation_Controller leController;


    public Zulieferer(Land land, int[] komponenten, int mlm, int zuliefererID, TagArt[] tagArts, Data_Manipulation_Controller leController) {
        myLand = land;
        this.komponenten = komponenten;
        mindestLiefermenge = mlm;
        rueckstand = new int[komponenten.length];
        this.zuliefererID = zuliefererID;
        this.tagArts = tagArts;
        this.leController = leController;
        alleLieferungen = new LinkedList<Lieferung>();
    }

    public int getNextPossibleLiefertag() {

        return -1;
    }

    /**
     * gibt zurück, ob eine Lieferung von diesem zulieferer noch im legalen änderungszeitraum liegt
     * @param lieferung die lieferung, die geprüft werden soll
     * @return true wenn möglich, false wenn nicht
     */
    public boolean canChangeLieferung(Lieferung lieferung) {
        Schichtarbeitstag starttag = leController.getAlleTage()[lieferung.getStarttag_ID()];
        while (starttag.isHoldayIn(myLand)) {
            starttag = starttag.getVortag();
        }
        int starttagID = starttag.getArbeitstag_ID();
        int heuteId = leController.getToday(0);

        if((starttagID - heuteId) > (tagArts[0].getAnzahlTage() + tagArts[1].getAnzahlTage())){
            return true;
        }
        return false;
    }

        public int getZuliefererID () {
            return zuliefererID;
        }

        public void setZuliefererID ( int zuliefererID){
            this.zuliefererID = zuliefererID;
        }

        public List<Lieferung> getAlleLieferungen () {
            return alleLieferungen;
        }

        public void setAlleLieferungen (List < Lieferung > alleLieferungen) {
            this.alleLieferungen = alleLieferungen;
        }

        public int getMindestLiefermenge () {
            return mindestLiefermenge;
        }

        public Land getMyLand () {
            return myLand;
        }

        public void setMyLand (Land myLand){
            this.myLand = myLand;
        }

        public int[] getKomponenten () {
            return komponenten;
        }

        public void setKomponenten ( int[] komponenten){
            this.komponenten = komponenten;
        }

        public int getRueckstand ( int komponente){
            return rueckstand[komponente - 1];
        }

        public void setRueckstand ( int rueckstand, int komponente){
            this.rueckstand[komponente] = rueckstand;
        }

        public Lieferung neueLieferungAnlegen ( int komponentID, int anzahl, int ankunftstagID){
            int index = idToIndex(komponentID);
            if (index == -1) return null;
            //if (anzahl + this.rueckstand[index] < this.mindestLiefermenge) {
            //    rueckstand[index] = rueckstand[index] + anzahl;
            //    return null;
            //}
            if(anzahl< this.mindestLiefermenge){
                return null;
            }

            int startagId = ankunftstagID;
            TagArt eins = new TagArt(tagArts[0].gettype(), this.myLand, tagArts[0].getAnzahlTage());
            TagArt zwei = new TagArt(tagArts[1].gettype(), this.myLand, tagArts[1].getAnzahlTage());
            startagId = reduceDays(startagId, eins);
            startagId = reduceDays(startagId, zwei);
            if(startagId>= leController.getToday(0)){
                return null;
            }

            Lieferung returnvalue = new Lieferung(komponentID, anzahl, ankunftstagID, startagId, leController.getAlleTage()[ankunftstagID].getDatum(), leController.getAlleTage()[startagId].getDatum());
            alleLieferungen.add(returnvalue);
            return returnvalue;
        }

        private int idToIndex ( int komponentID){
            switch (komponentID) {
                case 9:
                    return 0;
                case 10:
                    return 1;
                case 11:
                    return 2;
                case 12:
                    return 3;
                case 13:
                    return 0;
                case 14:
                    return 1;
                case 15:
                    return 2;
                case 16:
                    return 0;
                case 17:
                    return 1;
                case 18:
                    return 2;
                case 19:
                    return 3;
                case 20:
                    return 4;
                case 21:
                    return 5;
                case 22:
                    return 6;
            }
            return -1;
        }

        private int reduceDays ( int tagID, TagArt tagArt){
            while (!tagArt.isFinished()) {
                if (tagArt.gettype() == TagArtTyp.Arbeitstag) {
                    tagID--;
                    tagArt.reduceDay();
                } else {
                    if (!leController.getAlleTage()[tagID].isHoldayIn(this.myLand)) {
                        tagArt.reduceDay();
                    }
                    tagID--;
                }
            }
            return tagID;
        }


    }
