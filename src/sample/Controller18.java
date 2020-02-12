package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller18 extends Controller_Base  {
    @FXML
    private Label datum;

    @FXML
    private TableView<ProduktionHeuteAnzeige> produktionHeute;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Allrounder;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Competition;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Downhill;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Extreme;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Freeride;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Marathon;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Performance;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Trail;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Tundra;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Raceline;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Spark;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Speedline;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> a7005DB;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> a7005TB;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Monocoque;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> F100;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> F80;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Talas;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Reba;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> f351;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> SL;
    @FXML
    private TableColumn<ProduktionHeuteAnzeige, String> Raidon;

    private final ObservableList<ProduktionHeuteAnzeige> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }


    public void erstellen_button(Event evt) {
        Button b = (Button) evt.getSource();

            String[][] prodArray = backendInterface.produktionTag(backendInterface.getHeute());

            String text = "";
            data.removeAll(data);
            produktionHeute.setItems(data);
            //private int iNumber = 1:

            if (prodArray != null) {
                for (int i = 0; i < prodArray.length; i++) {
                    ProduktionHeuteAnzeige l = new ProduktionHeuteAnzeige(prodArray[i][0], prodArray[i][1], prodArray[i][2],
                            prodArray[i][3], prodArray[i][4], prodArray[i][5], prodArray[i][6], prodArray[i][7]);
                    data.addAll(l);
                }

                Allrounder.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Allrounder"));
                Competition.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Competition"));
                Downhill.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Downhill"));
                Extreme.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Extreme"));
                Freeride.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Freeride"));
                Marathon.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Marathon"));
                Performance.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Performance"));
                Trail.setCellValueFactory(new PropertyValueFactory<ProduktionHeuteAnzeige, String>("Trail"));

                produktionHeute.setItems(data);




        }

        // Hide this current window (if this is what you want)
        //((Node) (evt.getSource())).getScene().getWindow().hide();


    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }



}
