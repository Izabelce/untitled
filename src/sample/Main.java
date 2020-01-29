package sample;

import Data_Manipulation_Module.Data_Manipulation_Controller;
import Database_Connectivity_Module.Database_Connector;
import Database_Connectivity_Module.Database_Controller;
import Database_Connectivity_Module.Database_Helper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("Building Database_Connectivity_Module...");
        Database_Connector dbcon = new Database_Connector();
        Database_Helper dbHelper = new Database_Helper();
        Database_Controller dbCtrl = new Database_Controller(dbcon, dbHelper);
        dbcon.populate_Tablespace();
        //########################################################################

        System.out.println("Building Data_Manipulation_Module...");
        Data_Manipulation_Controller dbManCon = new Data_Manipulation_Controller(dbCtrl.getDbInterface());
        dbManCon.zielVorgabenWorkflow();
        //dbManCon.testLetzterArbeitstag();
        //########################################################################

        System.out.println("Building User Interface Module...");
        Backend_Interface b_Interface = new Backend_Interface(dbManCon.getManInterface());
        System.out.println("Welcome!");
        //dbManCon.getManInterface().test();




        Parent root = FXMLLoader.load(getClass().getResource("view1.fxml"));

        primaryStage.setTitle("Adventureworks_Bikes Version 2");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.setMaximized(true);
        primaryStage.show();


    }






    public static void main(String[] args) {
        launch(args);
    }
}