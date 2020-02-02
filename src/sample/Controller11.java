package sample;

import Database_Connectivity_Module.Lieferung;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Controller11 extends Controller_Base{

    @FXML
    private DatePicker dateNew;

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }


    //TODO in controller 11,12,13 werden nur Daten angezeigt, hierfür methoden

    public void datum_OK(Event evt) {
        Button b = (Button) evt.getSource();

        if(dateNew.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
           // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        }else{
            //TODO HERE
            java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dateNew.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println(ft.format(gettedDatePickerDate));
            String date = "01.01.2001";//TODO HIER DEFAULT DATUM! NACH TEST ENTFERNEN
            TextArea textArea = new TextArea();
        }

    }


    }
