package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Controller10 extends Controller_Base {

    @FXML
    private Label datum;
    @FXML
    private Label anzahl;

    //TODO Datum wird gesetzt TESTEN!
    public void initialize() {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void einpflegen(Event evt) {
        Parent root1;
        anzahl.getText(); //TODO Anzahl Lagerschadennr. irgendwo rein schreiben bitte
        //TODO Daten einpflegen
        //Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

}
