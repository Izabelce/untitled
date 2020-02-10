package Database_Connectivity_Module;

import Data_Manipulation_Module.Data_Manipulation_Controller;
import Data_Manipulation_Module.Komponenttyp;
import Data_Manipulation_Module.Land;

public class ZuliefererManager {
    private static Zulieferer[] zulieferer = new Zulieferer[3];
    private static Data_Manipulation_Controller leController;

    public static void enlistController(Data_Manipulation_Controller c) {
        ZuliefererManager.leController = c;
    }

    public static Zulieferer getZulieferer(Land l) {
        switch (l) {
            case China:
                if (zulieferer[0] == null) {
                    int[] komponenten = {9, 10, 11, 12};
                    TagArt[] tagarts = new TagArt[2];
                    tagarts[0] = new TagArt(TagArtTyp.Kalendertag, Land.China, 31);
                    tagarts[1] = new TagArt(TagArtTyp.Arbeitstag, Land.China, 18);
                    zulieferer[0] = new Zulieferer(Land.China, komponenten, 500, 1, tagarts,leController);
                }
                return zulieferer[0];
            case Baden_Württemberg:
                if (zulieferer[1] == null) {
                    int[] komponenten = {13, 14, 15};
                    TagArt[] tagarts = new TagArt[2];
                    //3 tage lieferzeit, 7 tage vorlaufzeit
                    tagarts[0] = new TagArt(TagArtTyp.Arbeitstag, Land.Baden_Württemberg, 7);
                    tagarts[1] = new TagArt(TagArtTyp.Arbeitstag, Land.Baden_Württemberg, 3);
                    zulieferer[1] = new Zulieferer(Land.Baden_Württemberg, komponenten, 10, 2, tagarts, leController);
                }
                return zulieferer[1];
            case Spanien:
                if (zulieferer[2] == null) {
                    int[] komponenten = {16, 17, 18, 19, 20, 21, 22};
                    TagArt[] tagarts = new TagArt[2];
                    tagarts[0] = new TagArt(TagArtTyp.Kalendertag, Land.Spanien, 9);
                    tagarts[1] = new TagArt(TagArtTyp.Arbeitstag, Land.Spanien, 14);
                    zulieferer[2] = new Zulieferer(Land.Spanien, komponenten, 75, 3, tagarts, leController);
                }
                return zulieferer[2];
        }
        return null;
    }

    public static Zulieferer getZulieferer(int komponentenID) {
        switch (komponentenID) {
            case 9:
            case 10:
            case 11:
            case 12:
                return getZulieferer(Land.China);
            case 13:
            case 14:
            case 15:
                return getZulieferer(Land.Baden_Württemberg);
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                return getZulieferer(Land.Spanien);
        }
        return null;
    }

    public static Zulieferer getZulieferer(Komponenttyp k) {
        switch (k) {
            case SATTEL:
                return getZulieferer(Land.China);
            case GABEL:
                return getZulieferer(Land.Spanien);
            case RAHMEN:
                return getZulieferer(Land.Baden_Württemberg);
        }
        return null;
    }


}
