package sample;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.Event;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;
import java.util.ResourceBundle;

public class Controller1 implements Initializable {
private  interfaceDatum heute;
    @FXML
    private TextField textField;
    @FXML
    private Label datumLabel;
    @FXML
    private Button btnOpenNewWindow;


    public void initialize() {

        datumLabel.setText("PLATZHALTER");

    }
    public void button_demoNeu(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view2.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2");
            stage.setScene(new Scene(root1, 450, 450));
            stage.show();
            stage.setMaximized(true);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void button_demoCont(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view2.fxml"));
            Stage stage = new Stage();
            stage.isFullScreen();
            stage.setTitle("Adventureworks_Bikes Version 2");

            stage.setScene(new Scene(root1, 450, 450));

            stage.show();
            stage.setMaximized(true);


        }
        catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();
    }


    public void setDatum(interfaceDatum datumHeute) {
        heute= datumHeute;
        datumLabel.setText(heute.getHeute());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
