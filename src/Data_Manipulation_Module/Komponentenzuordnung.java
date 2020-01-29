package Data_Manipulation_Module;

public class Komponentenzuordnung {

    public static int getRahmenID(int fahrrad_ID) {
        switch (fahrrad_ID) {
            case 1:
                return 13;
            case 2:
                return 15;
            case 3:
                return 14;
            case 4:
                return 15;
            case 5:
                return 14;
            case 6:
                return 13;
            case 7:
                return 14;
            case 8:
                return 15;
        }
        return -1;
    }

    public static int getSattelID(int fahrrad_ID) {
        switch (fahrrad_ID) {
            case 1:
                return 11;
            case 2:
                return 12;
            case 3:
                return 9;
            case 4:
                return 11;
            case 5:
                return 9;
            case 6:
                return 10;
            case 7:
                return 9;
            case 8:
                return 12;
        }
        return -1;
    }

    public static int getGabelID(int fahrrad_ID) {
        switch (fahrrad_ID) {
            case 1:
                return 16;
            case 2:
                return 18;
            case 3:
                return 20;
            case 4:
                return 19;
            case 5:
                return 17;
            case 6:
                return 21;
            case 7:
                return 19;
            case 8:
                return 22;
        }
        return -1;
    }

    public static int getMBW(Komponenttyp ktyp) {
        if (ktyp == null) return -1;
        switch (ktyp) {
            case GABEL:
                return 75;
            case RAHMEN:
                return 10;
            case SATTEL:
                return 500;
        }
        return -1;
    }

    public static int getZulieferer(int komponentID) {

        switch (komponentID) {
            case 9:
            case 10:
            case 11:
            case 12:
                return 1;

            case 13:
            case 14:
            case 15:
                return 2;

            default:
                return 3;
        }
    }

    public static Komponenttyp getKtypFromID(int id) {
        switch (id) {
            case 9:
            case 10:
            case 11:
            case 12: return  Komponenttyp.SATTEL;
            case 13:
            case 14:
            case 15:return Komponenttyp.RAHMEN;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22: return Komponenttyp.GABEL;
        }
        return null;
    }
}
