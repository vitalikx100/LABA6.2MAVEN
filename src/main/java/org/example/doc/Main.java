package org.example.doc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static TabulatedFunctionDoc tabFDoc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        tabFDoc = new TabulatedFunctionDoc();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMainForm.fxml"));
        Parent root = loader.load();
        FXMLMainFormController ctrl = loader.getController();
        ctrl.setStage(stage);
        tabFDoc.registerRedrawFunctionController(ctrl);
        Scene scene = new Scene(root);
        stage.setTitle("Tabulated Functions");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}