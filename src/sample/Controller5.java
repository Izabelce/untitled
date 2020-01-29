package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Controller5 extends Controller_Base {

    @FXML
    private Label datum;
    @FXML
    private DatePicker zeitraumVon;
    @FXML
    private DatePicker zeitraumBis;

    //TODO Datum wird gesetzt TESTEN!
    public void initialize() {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }

    public void erstellen_button(Event evt) {
        Button b = (Button) evt.getSource();
        //LocalDate value = dateNew.getValue();
        java.sql.Date getZeitraumVon = java.sql.Date.valueOf(zeitraumVon.getValue());
        java.sql.Date getZeitraumBis = java.sql.Date.valueOf(zeitraumBis.getValue());
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
        //System.out.println(ft.format(gettedDatePickerDate));
        // TODO hier wird das neue ausgewählte Datum in das Interface Instanz geschrieben, um es später zuzugreifen
        //TODO BITTE EINE METHODE ZUR ÜBERGABE SCHREIBEN
        //backendInterface.setHeute(ft.format(gettedDatePickerDate));

        //TODO HIER SOLL EIN PDF ERSTELLT WERDEN?
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view2.fxml"));
            Parent root = (Parent) loader.load();

            //Controller2 c2 = loader.getController();
            //c2.setDatum(ft.format(gettedDatePickerDate));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.isMaximized();
            stage.show();

        }

        catch (IOException e){
            e.printStackTrace();
        }




    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }



}
