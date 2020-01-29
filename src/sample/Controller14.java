package sample;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Controller14 extends Controller_Base {

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void bestellen(Event evt) {
        Parent root1;
        //Bestellung in der Datenbank schreiben
        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }
}
