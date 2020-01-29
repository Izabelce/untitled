package sample;

import Data_Manipulation_Module.Data_Manipulation_Interface;
import Database_Connectivity_Module.Bestellung;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Backend_Interface {
    private Data_Manipulation_Interface dataInterface;
    private static Backend_Interface single_instance = null;

        private Backend_Interface (Data_Manipulation_Interface data_interface) {
            this.dataInterface = data_interface;
        }

        public String getHeute(){
            return  dataInterface.getToday();
        }

        //TO-DO Methode set Heute implementieren
        public void setHeute(String heute){

        }

        public Bestellung neueBestellungAnlegen(int fahrradID, String wunschliefertermin, int anzahl){
          return(dataInterface.neueBestellungVersuchen(fahrradID, anzahl, wunschliefertermin));
        }

        public static Backend_Interface getInstance(Data_Manipulation_Interface dataInterface){
        if(dataInterface != null && single_instance != null){
            single_instance = new Backend_Interface(dataInterface);
        }
            return single_instance;
        }

    }


