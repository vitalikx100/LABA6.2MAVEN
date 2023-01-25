package org.example.doc;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ActionsDialogWindow {
    private Stage stage;

    private FXMLDialogController curCtrl;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    public FXMLDialogController.BUTTON showDialog(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXMLDialog.fxml"));
        Parent root = loader.load();
        FXMLDialogController ctrl = loader.getController();
        Scene scene = new Scene(root);
        Stage stg = new Stage();
        stg.setTitle("Functions parameters");
        stg.setScene(scene);
        stg.setResizable(false);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.initOwner(primaryStage);
        ctrl.setStage(stg);

        stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                ctrl.setLastBtn(FXMLDialogController.BUTTON.CANCEL);
            }
        });

        stg.showAndWait();

        return ctrl.getLastBtn();
    }
}
