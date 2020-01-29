package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller4 extends interfaceDatum implements Initializable {

    @FXML
    private Label datum;

    public Controller4(String datum) {
        super(datum);
    }


    public void button_close(Event evt) {
        Parent root1;

            // Hide this current window (if this is what you want)
            ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void geheZuLief(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view11.fxml"));
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

    public void geheZuBestell(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view12.fxml"));
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

    public void infos(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view13.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
