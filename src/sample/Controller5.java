package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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


            //TODO HIER SOLL EIN PDF ERSTELLT WERDEN?
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view2.fxml"));
                Parent root = (Parent) loader.load();

                //Controller2 c2 = loader.getController();
                //c2.setDatum(ft.format(gettedDatePickerDate));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.isMaximized();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // Hide this current window (if this is what you want)
        ((Node) (evt.getSource())).getScene().getWindow().hide();


    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }



}
