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
            case 12:
                return Komponenttyp.SATTEL;
            case 13:
            case 14:
            case 15:
                return Komponenttyp.RAHMEN;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                return Komponenttyp.GABEL;
        }
        return null;
    }


    public static String getCanonicalName(int lagerID) {
        switch (lagerID) {
            case 1:
                return "MTBAllrounder";
            case 2:
                return "MTBCompetition";
            case 3:
                return "MTBDownhill";
            case 4:
                return "MTBExtreme";
            case 5:
                return "MTBFreeride";
            case 6:
                return "MTBMarathon";
            case 7:
                return "MTBPerformance";
            case 8:
                return "MTBTrail";
            case 9:
                return "Aluminium 7005DB";
            case 10:
                return "Aluminium 7005TB";
            case 11:
                return "Carbon Monocoque";
            case 12:
                return "Fizik Tundra";
            case 13:
                return "Race Line";
            case 14:
                return "Spark";
            case 15:
                return "Speed Line";
            case 16:
                return "Fox32 F100";
            case 17:
                return "Fox32 F80";
            case 18:
                return "Fox Talas140";
            case 19:
                return "Rock Schox Reba";
            case 20:
                return "Rock Schox Recon351";
            case 21:
                return "Rock Schox ReconSL";
            case 22:
                return "SR Suntour Raidon";
        }
        return "ERROR";
    }


    public static int[] getFahrräderZurKomponente(int komponenttypID) {
        boolean[] fahrräder = new boolean[8];
        int anzahl = 0;
        switch (komponenttypID) {
            case 9:
                fahrräder[2] = true;
                fahrräder[4] = true;
                fahrräder[6] = true;
                anzahl = 3;
                break;
            case 10:
                fahrräder[5] = true;
                anzahl = 1;
                break;
            case 11:
                fahrräder[0] = true;
                fahrräder[3] = true;
                anzahl = 2;
                break;
            case 12:
                fahrräder[1] = true;
                fahrräder[7] = true;
                anzahl = 2;
                break;
            case 13:
                fahrräder[0] = true;
                fahrräder[5] = true;
                anzahl = 2;
                break;
            case 14:
                fahrräder[2] = true;
                fahrräder[4] = true;
                fahrräder[6] = true;
                anzahl = 3;
                break;
            case 15:
                fahrräder[1] = true;
                fahrräder[3] = true;
                fahrräder[7] = true;
                anzahl = 3;
                break;
            case 16:
                fahrräder[0] = true;
                anzahl = 1;
                break;
            case 17:
                fahrräder[4] = true;
                anzahl = 1;
                break;
            case 18:
                fahrräder[1] = true;
                anzahl = 1;
                break;
            case 19:
                fahrräder[3] = true;
                fahrräder[6] = true;
                anzahl = 2;
                break;
            case 20:
                fahrräder[2] = true;
                anzahl = 1;
                break;
            case 21:
                fahrräder[5] = true;
                anzahl = 1;
                break;
            case 22:
                fahrräder[7] = true;
                anzahl = 1;
                break;
        }
        int[] returnarray = new int[anzahl];
        int index = 0;
        for (int i = 0; i < fahrräder.length; i++) {
            if (fahrräder[i]) {
                returnarray[index] = i + 1;
                index++;
            }
        }

        return returnarray;
    }
}