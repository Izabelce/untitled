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

        public Backend_Interface (Data_Manipulation_Interface data_interface) {
            this.dataInterface = data_interface;
        }

        public String getHeute(){
            return  dataInterface.getToday();
        }

        public Bestellung neueBestellungAnlegen(int fahrradID, String wunschliefertermin, int anzahl){

          return(dataInterface.neueBestellungVersuchen(fahrradID, anzahl, wunschliefertermin));
        }

    }


