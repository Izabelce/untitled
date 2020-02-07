package sample;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller14 extends Controller_Base {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
    }
    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void bestellen(Event evt) {
        Parent root1;
        //TODO Bestellung in der Datenbank schreiben
        //Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }
}
