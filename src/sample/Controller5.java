package sample;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;

public class Controller5 {

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }
}
