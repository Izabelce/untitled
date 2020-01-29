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
        System.out.println("Building User Interface Module...");
        Parent root = FXMLLoader.load(getClass().getResource("view1.fxml"));

        primaryStage.setTitle("Adventureworks_Bikes Version 2");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.setMaximized(true);
        primaryStage.show();



        //dbManCon.testLetzterArbeitstag();
        //########################################################################
        System.out.println("Welcome!");
        //dbManCon.getManInterface().test();

    }






    public static void main(String[] args) {
        launch(args);
    }
}