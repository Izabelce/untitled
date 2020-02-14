package sample;

import Database_Connectivity_Module.Stammdatenmanager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller20 implements Initializable {

    @FXML
    private TextField allrounder;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        allrounder.setText(Stammdatenmanager.getAnteilModell1());

    }
}
