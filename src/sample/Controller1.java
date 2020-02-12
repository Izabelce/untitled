package sample;

import Data_Manipulation_Module.Data_Manipulation_Controller;
import Data_Manipulation_Module.Data_Manipulation_Interface;
import Data_Manipulation_Module.Datamanipulation_data;
import Database_Connectivity_Module.Database_Connectivity_Interface;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    @FXML
    private ImageView logo;


    public void initialize() {

        try {
            Image image = new Image(new FileInputStream("C:\\Users\\Public\\logo_adventure_works.png"));

            //Setting the image view
            ImageView imageView = new ImageView(image);
            //Image image = new Image("file: C:\\Users\\Public\\logo_adventure_works.png");

            logo.setImage(image);
            logo.setCache(true);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }

    }

    public void button_demoNeu(Event evt) {
        //init die wichtigen objekte

        System.out.println("Building Database_Connectivity_Module...");
        Database_Connector dbcon = new Database_Connector();
        Database_Helper dbHelper = new Database_Helper();
        Database_Controller dbCtrl = new Database_Controller(dbcon, dbHelper);
        dbcon.populate_Tablespace();
        dbcon.fillStammdatenmanager();
        System.out.println("Building Data_Manipulation_Module...");
        Data_Manipulation_Controller dbManCon = new Data_Manipulation_Controller(dbCtrl.getDbInterface());
        Data_Manipulation_Interface manipulation_interface = new Data_Manipulation_Interface();
        manipulation_interface.enlistController(dbManCon);
        dbManCon.zielVorgabenWorkflow();
        Datamanipulation_data dbManData = new Datamanipulation_data(dbManCon);
        dbManData.writeInCSV();
        Backend_Interface b_int = Backend_Interface.getInstance(manipulation_interface);
//        manipulation_interface.test();

        //########################################################################

        Parent root1;
        try {
            root1 = FXMLLoader.load(getClass().getResource("view2.fxml"));
            Stage stage = new Stage();

            Scene scene = new Scene(root1, 450, 450);
            scene.getStylesheets().add(getClass().getResource("new.css").toExternalForm());


            stage.setTitle("Adventureworks_Bikes Version 2 - 2. Main Menu");
            stage.setScene(scene);
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
        Datamanipulation_data dataHelper = new Datamanipulation_data((dbManCon));
        dataHelper.loadFromCSV();
        Backend_Interface b_int = Backend_Interface.getInstance(manipulation_interface);
        Database_Connector dbconnector = new Database_Connector();
        Database_Helper dbHelper = new Database_Helper();
        Database_Controller dbcon = new Database_Controller(dbconnector, dbHelper, dbManCon.getAlleTage()[1].getDatum());
        Database_Connectivity_Interface dbconnectorI = new Database_Connectivity_Interface(dbconnector, dbcon);
        dbManCon.enlistDatabaseConnectivity_Interface(dbconnectorI);

        try {
            root1 = FXMLLoader.load(getClass().getResource("view2.fxml"));

            Stage stage = new Stage();

            Scene scene = new Scene(root1, 450, 450);
            scene.getStylesheets().add(getClass().getResource("new.css").toExternalForm());



            stage.setTitle("Adventureworks_Bikes Version 2 - 2. Main Menu");
            stage.setScene(scene);
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
