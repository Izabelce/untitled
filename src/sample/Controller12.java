package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller12 extends Controller_Base{

    @FXML
    private DatePicker dateNew;

    @FXML
    private Label datum;

    @FXML
    private TableView<BestellungAnzeigeHelfer> bestellung;
    @FXML
    private TableColumn<BestellungAnzeigeHelfer, String> bestellungstag;
    @FXML
    private TableColumn<BestellungAnzeigeHelfer, String> abholungstag;
    @FXML
    private TableColumn<BestellungAnzeigeHelfer, String> anzahl;
    @FXML
    private TableColumn<BestellungAnzeigeHelfer, String> modell;

    private final ObservableList<BestellungAnzeigeHelfer> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        Date date = null;
        try {
            date = formatter.parse(backendInterface.getHeute());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateNew.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void datum_OK(Event evt) {
        Button b = (Button) evt.getSource();
        if (dateNew.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
            // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        } else {
            //LocalDate value = dateNew.getValue();
            java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dateNew.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println(ft.format(gettedDatePickerDate));

            String[][] bestellungenArray = backendInterface.getBestellwerte(ft.format(gettedDatePickerDate));

            String text = "";
            //private int iNumber = 1:
            data.removeAll(data);
            bestellung.setItems(data);

            if (bestellungenArray != null) {
                for (int i = 0; i < bestellungenArray.length; i++) {
                    BestellungAnzeigeHelfer l = new BestellungAnzeigeHelfer(bestellungenArray[i][0], bestellungenArray[i][1], bestellungenArray[i][2], bestellungenArray[i][3]);
                    data.addAll(l);
                }

                bestellungstag.setCellValueFactory(new PropertyValueFactory<BestellungAnzeigeHelfer, String>("bestellungstag"));
                abholungstag.setCellValueFactory(new PropertyValueFactory<BestellungAnzeigeHelfer, String>("abholungstag"));
                anzahl.setCellValueFactory(new PropertyValueFactory<BestellungAnzeigeHelfer, String>("modell"));
                modell.setCellValueFactory(new PropertyValueFactory<BestellungAnzeigeHelfer, String>("anzahl"));

                bestellung.setItems(data);
            }else {
                bestellung.setItems(null);
            }
        }
    }
}
