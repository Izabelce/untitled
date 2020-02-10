package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller3 extends Controller_Base implements Initializable {
    @FXML
    private DatePicker dateNew;

    @FXML
    private Label datum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }

    public void button_close(Event evt) {
        Parent root1;

        ((Node)(evt.getSource())).getScene().getWindow().hide();

    }

    public void datum_OK(Event evt) {
        Button b = (Button) evt.getSource();

        if(dateNew.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültige Eingabe");
            alert.setHeaderText("Bitte ein gültiges Datum auswählen");
            // alert.setContentText("Ooops, there was an error!");

            alert.showAndWait();
        }else {
            LocalDate value = dateNew.getValue();


            java.sql.Date gettedDatePickerDate = java.sql.Date.valueOf(dateNew.getValue());
            SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");
            System.out.println(ft.format(gettedDatePickerDate));
            //TODO hier wird das neue ausgewählte Datum in das Interface Instanz geschrieben, um es später zuzugreifen METHODE IMPEMENTIEREN
            backendInterface.setHeute(ft.format(gettedDatePickerDate));


            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view2.fxml"));
                Parent root = (Parent) loader.load();

                //Controller2 c2 = loader.getController();
                //c2.setDatum(ft.format(gettedDatePickerDate));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.isMaximized();
                stage.isFullScreen();
                stage.setTitle("Adventureworks_Bikes Version 2 - 2. Main Menu");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Hide this current window (if this is what you want)
            ((Node) (evt.getSource())).getScene().getWindow().hide();
        }



    }


};

