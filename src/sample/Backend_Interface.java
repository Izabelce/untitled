package sample;

import Data_Manipulation_Module.Data_Manipulation_Interface;
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

    }


