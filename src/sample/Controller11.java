package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.text.SimpleDateFormat;

public class Controller11 extends Controller_Base{

    @FXML
    private DatePicker dateNew;

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void datum_OK(Event evt) {
        Button b = (Button) evt.getSource();
        //LocalDate value = dateNew.getValue();
        java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dateNew.getValue());
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println(ft.format(gettedDatePickerDate));
    }


    }
