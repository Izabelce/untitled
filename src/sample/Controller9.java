package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller9 extends Controller_Base {

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

    public void verspaeteteLieferung(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view15.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 15. Verspätete Lieferung");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void maschienenausfall(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view16.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 16. Maschinenausfall");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void ladungsverlust(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view17.fxml"));
            Stage stage = new Stage();

            Scene scene = new Scene(root1, 450, 450);
            scene.getStylesheets().add(getClass().getResource("new.css").toExternalForm());
            stage.setTitle("Adventureworks_Bikes Version 2 - 17. Ladungsverlust");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();
    }
}
