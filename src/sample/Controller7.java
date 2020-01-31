package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Controller7 extends Controller_Base {


    @FXML
    private DatePicker liefertermin;
    @FXML
    private TextField ft1;
    @FXML
    private TextField ft2;
    @FXML
    private TextField ft3;
    @FXML
    private TextField ft4;
    @FXML
    private TextField ft5;
    @FXML
    private TextField ft6;
    @FXML
    private TextField ft7;
    @FXML
    private TextField ft8;



    public void bestellungPruefen(Event evt) {
        Parent root1;
        Button b = (Button) evt.getSource();
        //LocalDate value = dateNew.getValue();
        java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(liefertermin.getValue());
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        //System.out.println(ft.format(gettedDatePickerDate));

        //TODO Bestellung anlegen, bitte prüfen ob es so funktinioert
        backendInterface.neueBestellungAnlegen(1,ft.format(gettedDatePickerDate),Integer.valueOf(ft1.getText()));
        backendInterface.neueBestellungAnlegen(2,ft.format(gettedDatePickerDate),Integer.valueOf(ft2.getText()));
        backendInterface.neueBestellungAnlegen(3,ft.format(gettedDatePickerDate),Integer.valueOf(ft3.getText()));
        backendInterface.neueBestellungAnlegen(4,ft.format(gettedDatePickerDate),Integer.valueOf(ft4.getText()));
        backendInterface.neueBestellungAnlegen(5,ft.format(gettedDatePickerDate),Integer.valueOf(ft5.getText()));
        backendInterface.neueBestellungAnlegen(6,ft.format(gettedDatePickerDate),Integer.valueOf(ft6.getText()));
        backendInterface.neueBestellungAnlegen(7,ft.format(gettedDatePickerDate),Integer.valueOf(ft7.getText()));
        backendInterface.neueBestellungAnlegen(8,ft.format(gettedDatePickerDate),Integer.valueOf(ft8.getText()));

        try {
            root1 = FXMLLoader.load(getClass().getResource("view14.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }
}
