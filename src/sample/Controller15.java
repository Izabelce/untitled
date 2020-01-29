package sample;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Controller15 extends Controller_Base{

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void aenderungSpeichern(Event evt) {
        Parent root1;
        //TODO Daten einpflegen
        //TODO DATEN AUSLESEN AUS CHECKBOX UND DATUMFELDER
        //Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }
}
