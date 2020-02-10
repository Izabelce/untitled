package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller11 extends Controller_Base {

    @FXML
    private DatePicker dateNew;

    //TODO diese ausgegraute methode benutzen
    @FXML
    private TableView<LieferungAnzeigeHelfer> lieferung;
    @FXML
    private TableColumn<LieferungAnzeigeHelfer, String> ankunftstag;
    @FXML
    private TableColumn<LieferungAnzeigeHelfer, String> komponente;
    @FXML
    private TableColumn<LieferungAnzeigeHelfer, String> anzahl;
    @FXML
    private TableColumn<LieferungAnzeigeHelfer, String> istEingetroffen;
    @FXML
    private TableColumn<LieferungAnzeigeHelfer, String> erfassungstag;

    private final ObservableList<LieferungAnzeigeHelfer> data = FXCollections.observableArrayList();

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

    @FXML
    private Label datum;


    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node) (evt.getSource())).getScene().getWindow().hide();

    }


    //TODO in controller 11,12,13 werden nur Daten angezeigt, hierfür methoden

    public void datum_OK(Event evt) {
        Button b = (Button) evt.getSource();

        if (dateNew.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
            // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        } else {
            //TODO HERE
            java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dateNew.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println(ft.format(gettedDatePickerDate));
            //String date = "01.01.2001";//TODO HIER DEFAULT DATUM! NACH TEST ENTFERNEN
            String[][] lieferungenArray = backendInterface.getLieferwerte(ft.format(gettedDatePickerDate));

            String text = "";
            data.removeAll(data);
            lieferung.setItems(data);
            //private int iNumber = 1:
            if (lieferungenArray != null) {
                for (int i = 0; i < lieferungenArray.length; i++) {
                    LieferungAnzeigeHelfer l = new LieferungAnzeigeHelfer(lieferungenArray[i][0], lieferungenArray[i][1], lieferungenArray[i][2], lieferungenArray[i][3], lieferungenArray[i][4]);
                    data.addAll(l);
                }

                ankunftstag.setCellValueFactory(new PropertyValueFactory<LieferungAnzeigeHelfer, String>("ankunftstag"));
                komponente.setCellValueFactory(new PropertyValueFactory<LieferungAnzeigeHelfer, String>("komponente"));
                anzahl.setCellValueFactory(new PropertyValueFactory<LieferungAnzeigeHelfer, String>("anzahl"));
                istEingetroffen.setCellValueFactory(new PropertyValueFactory<LieferungAnzeigeHelfer, String>("istEingetroffen"));
                erfassungstag.setCellValueFactory(new PropertyValueFactory<LieferungAnzeigeHelfer, String>("erfassungstag"));

                //drei.setText(text);
                lieferung.setItems(data);
            }

        }
    }
}

