package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Controller12 extends Controller_Base{

    @FXML
    private DatePicker dateNew;

    @FXML
    private Label datum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void datum_OK(Event evt) {
        Button b = (Button) evt.getSource();
        if (dateNew.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
            // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        } else {
            //LocalDate value = dateNew.getValue();
            java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dateNew.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println(ft.format(gettedDatePickerDate));
        }
    }
}
