package Data_Manipulation_Module;

public class Plausibility {
    private Data_Manipulation_Controller myController;

    public Plausibility() {
    }

    public static boolean isWrongID(int fahrradID) {
        return(fahrradID<0 || fahrradID>9);
    }




    public void enlistController(Data_Manipulation_Controller con) {
        myController = con;
    }
}
