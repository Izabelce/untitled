package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller_Base implements Initializable {

    //Error fenster url: https://code.makery.ch/blog/javafx-dialogs-official/


    Backend_Interface backendInterface;
    @FXML
    private Label datum;
    @FXML
    private ImageView logo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());

        try {
            Image image = new Image(new FileInputStream("C:\\Users\\Public\\logo_adventure_works.png"));

            //Setting the image view
            ImageView imageView = new ImageView(image);
            //Image image = new Image("file: C:\\Users\\Public\\logo_adventure_works.png");

            logo.setImage(image);
            logo.setCache(true);
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }

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
