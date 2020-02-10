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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller2 extends Controller_Base implements Initializable {

    @FXML
    private TextField textField;
    @FXML
    private Label datum;
    @FXML
    private Button btnOpenNewWindow;




    public void changeDate(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view3.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 3. Datum ändern");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();
            ((Node)(evt.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)


    }

    public void infosHeute(Event evt) {
        Parent root1;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view4.fxml"));
            Parent root = (Parent) loader.load();
            //Controller4 c4 = loader.getController();
            //c4.setDatum(this.datum.getText());
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 4. Infos Heute");
            stage.setScene(new Scene(root, 450, 450));
            stage.setMaximized(true);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bestandsreportErstellen(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view5.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 5. Bestandsreport");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void scorReport(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view6.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 6. SCOR Metriken");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void bestellungNeu(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view7.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 7. Bestellung neu");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void marketingaktion(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view8.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 8. Marketingaktion");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void zuliefererProbleme(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view9.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 9. Zuliefererprobleme");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }

    public void lagerschaden(Event evt) {
        Parent root1;

        try {
            root1 = FXMLLoader.load(getClass().getResource("view10.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Adventureworks_Bikes Version 2 - 10. Lagerschaden");
            stage.setScene(new Scene(root1, 450, 450));
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Hide this current window (if this is what you want)
        //((Node)(evt.getSource())).getScene().getWindow().hide();
    }


}
