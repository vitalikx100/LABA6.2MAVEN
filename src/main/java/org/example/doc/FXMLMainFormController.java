package org.example.doc;

import org.example.functions.FunctionPoint;
import org.example.functions.InappropriateFunctionPointException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLMainFormController {
    @FXML
    private TextField edX;

    @FXML
    private TextField edY;

    @FXML
    private TableView<FunctionPointT> table;

    @FXML
    private TableColumn<FunctionPointT, Double> xColumn;

    @FXML
    private TableColumn<FunctionPointT, Double> yColumn;

    @FXML
    private Label numberLabel;

    @FXML
    private void initialize() {
        xColumn.setCellValueFactory(new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("Y"));

        redraw();
    }

    private Stage stage;

    @FXML
    private void btAddPointClick(ActionEvent av) {
        try {
            Main.tabFDoc.addPoint(new FunctionPoint(Double.parseDouble(edX.getText()),Double.parseDouble(edY.getText())));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
    }

    @FXML
    private void btDeleteClick(ActionEvent av) {
        try {
            int index = -1;
            for (int i = 0; i < Main.tabFDoc.getPointsCount(); ++i) {
                if (Main.tabFDoc.getPoint(i).equals(new FunctionPoint(Double.parseDouble(edX.getText()),Double.parseDouble(edY.getText()))))
                    index = i;
            }
            Main.tabFDoc.deletePoint(index);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.showAndWait();
        }
    }

    @FXML
    public void redraw() {
        ObservableList<FunctionPointT> data = table.getItems();
        data.clear();
        for (int i = 0; i < Main.tabFDoc.getPointsCount(); ++i) {
            data.add(new FunctionPointT(Main.tabFDoc.getPointX(i), Main.tabFDoc.getPointY(i)));
        }
        table.setItems(data);
    }

    @FXML
    private void mouseReleasedOnTable(){
        updatePointCount();
    }

    @FXML
    private void keyReleasedOnTable(){
        updatePointCount();
    }

    private void updatePointCount() {
        numberLabel.setText("Point " + Integer.toString(table.getSelectionModel().getSelectedIndex() + 1) + " of " + Integer.toString(Main.tabFDoc.getPointsCount()));
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void  btnNewFile() throws Exception {
        ActionsDialogWindow wind = new ActionsDialogWindow();
        FXMLDialogController.BUTTON lstBtn = wind.showDialog(stage);
    }
}
