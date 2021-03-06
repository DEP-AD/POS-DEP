package lk.ijse.pos.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;

public class DashBoardController {

    public AnchorPane contextOfDashBoard;

    public void openCustomerForm(MouseEvent mouseEvent) throws IOException {
        /**Stage stage = (Stage) contextOfDashBoard.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/CustomerForm.fxml"))));*/

        setUI("CustomerForm");
    }

    public void openItemForm(MouseEvent mouseEvent) throws IOException {
    /**    Stage stage = (Stage) contextOfDashBoard.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/ItemForm.fxml"))));*/

        setUI("ItemForm");
    }

    public void OpenOrderForm(MouseEvent mouseEvent) throws IOException {
        setUI("PlaceOrderForm");
    }

    public void OpenOrderListForm(MouseEvent mouseEvent) throws IOException {
        setUI("OrderListForm");
    }

    private void setUI(String location) throws IOException {
        Stage stage = (Stage) contextOfDashBoard.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/"+ location +".fxml"))));
    }



}
