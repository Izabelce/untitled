package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


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