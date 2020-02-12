package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class Controller7 extends Controller_Base {


    @FXML
    private DatePicker liefertermin;
    @FXML
    private TextField ft1;
    @FXML
    private TextField ft2;
    @FXML
    private TextField ft3;
    @FXML
    private TextField ft4;
    @FXML
    private TextField ft5;
    @FXML
    private TextField ft6;
    @FXML
    private TextField ft7;
    @FXML
    private TextField ft8;

    @FXML
    private Label datum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }



    public void bestellungPruefen(Event evt) {
        Parent root1;
        Button b = (Button) evt.getSource();
        //LocalDate value = dateNew.getValue();
        if (liefertermin.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
            // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        } else {
            java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(liefertermin.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            //System.out.println(ft.format(gettedDatePickerDate));

            //TODO Bestellung anlegen, bitte prüfen ob es so funktinioert
            backendInterface.neueBestellungAnlegen(1, ft.format(gettedDatePickerDate), Integer.valueOf(ft1.getText()));
            backendInterface.neueBestellungAnlegen(2, ft.format(gettedDatePickerDate), Integer.valueOf(ft2.getText()));
            backendInterface.neueBestellungAnlegen(3, ft.format(gettedDatePickerDate), Integer.valueOf(ft3.getText()));
            backendInterface.neueBestellungAnlegen(4, ft.format(gettedDatePickerDate), Integer.valueOf(ft4.getText()));
            backendInterface.neueBestellungAnlegen(5, ft.format(gettedDatePickerDate), Integer.valueOf(ft5.getText()));
            backendInterface.neueBestellungAnlegen(6, ft.format(gettedDatePickerDate), Integer.valueOf(ft6.getText()));
            backendInterface.neueBestellungAnlegen(7, ft.format(gettedDatePickerDate), Integer.valueOf(ft7.getText()));
            backendInterface.neueBestellungAnlegen(8, ft.format(gettedDatePickerDate), Integer.valueOf(ft8.getText()));

            try {
                root1 = FXMLLoader.load(getClass().getResource("view14.fxml"));
                Stage stage = new Stage();

                Scene scene = new Scene(root1, 450, 450);
                scene.getStylesheets().add(getClass().getResource("new.css").toExternalForm());
                stage.setTitle("Adventureworks_Bikes Version 2");
                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // Hide this current window (if this is what you want)
            ((Node)(evt.getSource())).getScene().getWindow().hide();
        }
    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }
}
