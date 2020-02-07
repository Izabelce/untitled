package Database_Connectivity_Module;

public class Fahrplan {
    private int[] abfahrtstage;
    public static Fahrplan instance;

    private Fahrplan (int[] abfahrtstage){
        this.abfahrtstage = abfahrtstage;
    }

    public static Fahrplan getInstance(int[] abfahrtstage) {
        if (instance == null) {
            instance = new Fahrplan(abfahrtstage);
        }
        return instance;
    }

    public int[] getAbfahrtstage(){
        return abfahrtstage;
    }

}
