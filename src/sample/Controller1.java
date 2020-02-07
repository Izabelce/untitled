package sample;

import Data_Manipulation_Module.Data_Manipulation_Controller;
import Data_Manipulation_Module.Data_Manipulation_Interface;
import Database_Connectivity_Module.Database_Connector;
import Database_Connectivity_Module.Database_Controller;
import Database_Connectivity_Module.Database_Helper;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller1 implements Initializable {
    private interfaceDatum heute;
    @FXML
    private TextField textField;
    @FXML
    private Label datumLabel;
    @FXML
    private Button btnOpenNewWindow;


    public void initialize() {

        datumLabel.setText("PLATZHALTER");

    }

    public void button_demoNeu(Event evt) {
        //init die wichtigen objekte

        System.out.println("Building Database_Connectivity_Module...");
        Database_Connector dbcon = new Database_Connector();
        Database_Helper dbHelper = new Database_Helper();
        Database_Controller dbCtrl = new Database_Controller(dbcon, dbHelper);
       // dbcon.populate_Tablespace();
        System.out.println("Building Data_Manipulation_Module...");
        Data_Manipulation_Controller dbManCon = new Data_Manipulation_Controller(dbCtrl.getDbInterface());
        Data_Manipulation_Interface manipulation_interface = new Data_Manipulation_Interface();
        manipulation_interface.enlistController(dbManCon);
        //dbManCon.zielVorgabenWorkflow();
        Backend_Interface b_int = Backend_Interface.getInstance(manipulation_interface);
//        manipulation_interface.test();

        //########################################################################

        Parent root1;
        try {
            root1 = FXMLLoader.load(getClass().getResource("view2.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2");
            stage.setScene(new Scene(root1, 450, 450));
            stage.show();
            stage.setMaximized(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node) (evt.getSource())).getScene().getWindow().hide();
    }

    public void button_demoCont(Event evt) {
        Parent root1;
        System.out.println("Building Data_Manipulation_Module...");
        Data_Manipulation_Controller dbManCon = new Data_Manipulation_Controller(new String[1]);
        Data_Manipulation_Interface manipulation_interface = new Data_Manipulation_Interface();
        manipulation_interface.enlistController(dbManCon);

        Backend_Interface b_int = Backend_Interface.getInstance(manipulation_interface);
        try {
            root1 = FXMLLoader.load(getClass().getResource("view2.fxml"));
            Stage stage = new Stage();
            stage.isFullScreen();
            stage.setTitle("Adventureworks_Bikes Version 2");

            stage.setScene(new Scene(root1, 450, 450));

            stage.show();
            stage.setMaximized(true);


        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node) (evt.getSource())).getScene().getWindow().hide();
    }


    public void setDatum(interfaceDatum datumHeute) {
        heute = datumHeute;
        datumLabel.setText(heute.getHeute());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
