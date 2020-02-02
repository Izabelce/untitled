package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller_Base implements Initializable {

    //Error fenster url: https://code.makery.ch/blog/javafx-dialogs-official/


    Backend_Interface backendInterface;
    @FXML
    private Label datum;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }

    public String getHeute() {
        return heute;
    }

    public void setDatum(String datumNew) {

        heute = datumNew;

    }

    private String heute;

    public Controller_Base(){
        this("01.01.2010");
    }

    public Controller_Base(String datum){
        heute = datum;
    }
}
