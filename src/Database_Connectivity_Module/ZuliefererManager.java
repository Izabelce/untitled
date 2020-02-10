package Database_Connectivity_Module;

import Data_Manipulation_Module.Komponenttyp;
import Data_Manipulation_Module.Land;

public class ZuliefererManager {
    private static Zulieferer[] zulieferer = new Zulieferer[3];

    public static Zulieferer getZulieferer(Land l) {
        switch (l) {
            case China:
                if (zulieferer[0] == null) {
                    int[] komponenten = {9, 10, 11, 12};
                    zulieferer[0] = new Zulieferer(Land.China, komponenten, 500, 1);
                }
                return zulieferer[0];
            case Baden_Württemberg:
                if (zulieferer[1] == null) {
                    int[] komponenten = {13, 14, 15};
                    zulieferer[1] = new Zulieferer(Land.Baden_Württemberg, komponenten, 10, 2);
                }
                return zulieferer[1];
            case Spanien:
                if (zulieferer[2] == null) {
                    int[] komponenten = {16, 17, 18, 19, 20, 21, 22};
                    zulieferer[2] = new Zulieferer(Land.Spanien, komponenten, 75, 3);
                }
                return zulieferer[2];
        }
        return null;
    }

    public static Zulieferer getZulieferer(int komponentenID){
        switch (komponentenID){
            case 9:
            case 10:
            case 11:
            case 12: return getZulieferer(Land.China);
            case 13:
            case 14:
            case 15: return getZulieferer(Land.Baden_Württemberg);
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22: return getZulieferer(Land.Spanien);
        }
        return null;
    }

    public static Zulieferer getZulieferer(Komponenttyp k){
        switch (k){
            case SATTEL: return getZulieferer(Land.China);
            case GABEL: return getZulieferer(Land.Spanien);
            case RAHMEN: return getZulieferer(Land.Baden_Württemberg);
        }
        return null;
    }



}
