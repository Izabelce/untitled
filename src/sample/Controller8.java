package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Controller8 extends Controller_Base {


    @FXML
    private DatePicker zeitraumVon;
    @FXML
    private DatePicker zeitraumBis;
    @FXML
    private DatePicker erwartetNachfrage;
    @FXML
    private CheckBox ft1;
    @FXML
    private CheckBox ft2;
    @FXML
    private CheckBox ft3;
    @FXML
    private CheckBox ft4;
    @FXML
    private CheckBox ft5;
    @FXML
    private CheckBox ft6;
    @FXML
    private CheckBox ft7;
    @FXML
    private CheckBox ft8;


    public void einpflegen(Event evt) {
        Parent root1;
        Button b = (Button) evt.getSource();
        //LocalDate value = dateNew.getValue();
        java.sql.Date getZeitraumVon = java.sql.Date.valueOf(zeitraumVon.getValue());
        java.sql.Date getZeitraumBis = java.sql.Date.valueOf(zeitraumBis.getValue());
        java.sql.Date getNachfrage = java.sql.Date.valueOf(erwartetNachfrage.getValue());
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");

        //TODO Methode da wo du die reinschreiben willst methode.ft.format(gettedDatePickerDate)

        //TODO Methode Wert von Checkbox nehmen



        //TODO Daten im System einpflegen


        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

}
