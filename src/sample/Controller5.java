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

public class Controller5 extends Controller_Base  {

    @FXML
    private DatePicker zeitraumVon;
    @FXML
    private DatePicker zeitraumBis;

    @FXML
    private Label datum;

    @FXML
    private TableView<BestandAnzeigeHelfer> bestand;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> tag;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Allrounder;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Competition;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Downhill;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Extreme;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Freeride;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Marathon;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Performance;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Trail;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Tundra;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Raceline;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Spark;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Speedline;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> a7005DB;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> a7005TB;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Monocoque;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> F100;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> F80;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Talas;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Reba;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> f351;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> SL;
    @FXML
    private TableColumn<BestandAnzeigeHelfer, String> Raidon;

    private final ObservableList<BestandAnzeigeHelfer> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }


    public void erstellen_button(Event evt) {
        Button b = (Button) evt.getSource();
        //LocalDate value = dateNew.getValue();

        if(zeitraumVon.getValue() == null || zeitraumBis.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
            // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        }else {
            java.sql.Date getZeitraumVon = java.sql.Date.valueOf(zeitraumVon.getValue());
            java.sql.Date getZeitraumBis = java.sql.Date.valueOf(zeitraumBis.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            String[][] bestandArray = backendInterface.bestandsreport(ft.format(getZeitraumVon),ft.format(getZeitraumBis));

            String text = "";
            data.removeAll(data);
            bestand.setItems(data);
            //private int iNumber = 1:

            if (bestandArray != null) {
                for (int i = 0; i < bestandArray.length; i++) {
                    BestandAnzeigeHelfer l = new BestandAnzeigeHelfer(bestandArray[i][0], bestandArray[i][1], bestandArray[i][2],
                            bestandArray[i][3], bestandArray[i][4], bestandArray[i][5], bestandArray[i][6], bestandArray[i][7],
                            bestandArray[i][8], bestandArray[i][9], bestandArray[i][10], bestandArray[i][11], bestandArray[i][12],
                            bestandArray[i][13], bestandArray[i][14], bestandArray[i][15], bestandArray[i][16], bestandArray[i][17],
                            bestandArray[i][18], bestandArray[i][19], bestandArray[i][20], bestandArray[i][21], bestandArray[i][22]);
                    data.addAll(l);
                }

                tag.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("tag"));
                Allrounder.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Allrounder"));
                Competition.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Competition"));
                Downhill.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Downhill"));
                Extreme.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Extreme"));
                Freeride.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Freeride"));
                Marathon.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Marathon"));
                Performance.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Performance"));
                Trail.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Trail"));
                Tundra.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Tundra"));
                Raceline.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Raceline"));
                Spark.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Spark"));
                Speedline.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Speedline"));
                a7005DB.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("a7005DB"));
                a7005TB.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("a7005TB"));
                Monocoque.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Monocoque"));
                F100.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("F100"));
                F80.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("F80"));
                Talas.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Talas"));
                Reba.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Reba"));
                f351.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("f351"));
                SL.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("SL"));
                Raidon.setCellValueFactory(new PropertyValueFactory<BestandAnzeigeHelfer, String>("Raidon"));

                bestand.setItems(data);
            }



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
