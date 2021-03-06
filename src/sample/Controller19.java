package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller19 extends Controller_Base {
    @FXML
    private Label datum;

    @FXML
    private TableView<ProdPlanungJahrAnzeige> produktionJahr;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> tag;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Allrounder;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Competition;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Downhill;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Extreme;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Freeride;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Marathon;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Performance;
    @FXML
    private TableColumn<ProdPlanungJahrAnzeige, String> Trail;


    private final ObservableList<ProdPlanungJahrAnzeige> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backendInterface = Backend_Interface.getInstance(null);
        datum.setText(backendInterface.getHeute());
    }


    public void jahr_button(Event evt) {
        Button b = (Button) evt.getSource();

        String[][] prodArray = backendInterface.produktionsplanungJahr(backendInterface.getHeute());
        //TODO AUS DEM HEUTE DAS JAHR ERMITTELN

        String text = "";
        data.removeAll(data);
        produktionJahr.setItems(data);
        //private int iNumber = 1:
        setValueFactories(prodArray);
    }

    public void monat_button(Event evt) {
        Button b = (Button) evt.getSource();

        String[][] prodArray = backendInterface.produktionsplanungMonat(backendInterface.getHeute());
        //TODO AUS DEM HEUTE DEN MONAT ERMITTELN

        String text = "";
        data.removeAll(data);
        produktionJahr.setItems(data);
        //private int iNumber = 1:
        setValueFactories(prodArray);
    }



    public void woche_button(Event evt) {
        Button b = (Button) evt.getSource();

        String[][] prodArray = backendInterface.produktionsplanungWoche(backendInterface.getHeute());
        //TODO AUS DEM HEUTE DIE WOCHE ERMITTELN

        String text = "";
        data.removeAll(data);
        produktionJahr.setItems(data);
        //private int iNumber = 1:
        setValueFactories(prodArray);

    }

    public void tag_button(Event evt) {
        Button b = (Button) evt.getSource();

        String[][] prodArray = backendInterface.produktionsplanungTag(backendInterface.getHeute());
        //TODO AUS DEM HEUTE DAS JAHR ERMITTELN

        String text = "";
        data.removeAll(data);
        produktionJahr.setItems(data);
        //private int iNumber = 1:
        setValueFactories(prodArray);
    }



    public void button_close(Event evt) {
        Parent root1;

        // Hide this current window (if this is what you want)
        ((Node) (evt.getSource())).getScene().getWindow().hide();

    }

    private void setValueFactories(String[][] prodArray) {
        if (prodArray != null) {
            for (int i = 0; i < prodArray.length; i++) {
                ProdPlanungJahrAnzeige l = new ProdPlanungJahrAnzeige(prodArray[i][0], prodArray[i][1], prodArray[i][2],
                        prodArray[i][3], prodArray[i][4], prodArray[i][5], prodArray[i][6], prodArray[i][7], prodArray[i][8]);
                data.addAll(l);
            }
            tag.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("tag"));
            Allrounder.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Allrounder"));
            Competition.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Competition"));
            Downhill.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Downhill"));
            Extreme.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Extreme"));
            Freeride.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Freeride"));
            Marathon.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Marathon"));
            Performance.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Performance"));
            Trail.setCellValueFactory(new PropertyValueFactory<ProdPlanungJahrAnzeige, String>("Trail"));

            produktionJahr.setItems(data);
        }
    }
}



