package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller10 extends Controller_Base {

    @FXML
    private TextField anzahl;

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

    public void einpflegen(Event evt) {
        Parent root1;
        anzahl.getText(); //TODO Anzahl Lagerschadennr. irgendwo rein schreiben bitte
        //TODO Daten einpflegen
        //Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

}
