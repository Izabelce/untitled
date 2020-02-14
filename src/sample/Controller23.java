package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller23 implements Initializable {
    @FXML
    private TableView<Zielmarkte> zielmarkte;

    @FXML
    private TableColumn<Zielmarkte, String> land;

    @FXML
    private TableColumn<Zielmarkte, String> anzahl;

    @FXML
    private ImageView logo;

    private final ObservableList<Zielmarkte> data = FXCollections.observableArrayList();



    private void fillUserList() {

        Zielmarkte zm1 = new Zielmarkte("Deutschland", "37%");
        Zielmarkte zm2 = new Zielmarkte("USA", "23%");
        Zielmarkte zm3 = new Zielmarkte("Frankreich", "18%");
        Zielmarkte zm4 = new Zielmarkte("China", "10%");
        Zielmarkte zm5 = new Zielmarkte("Schweiz", "6%");
        Zielmarkte zm6 = new Zielmarkte("Östereich", "6%");
        Zielmarkte zm7 = new Zielmarkte("SUMME", "100%");

        data.addAll(zm1, zm2, zm3, zm4, zm5, zm6, zm7);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillUserList();
        land.setCellValueFactory(new PropertyValueFactory<Zielmarkte, String>("land"));
        anzahl.setCellValueFactory(new PropertyValueFactory<Zielmarkte, String>("anteil"));

        zielmarkte.setItems(data);

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


}
