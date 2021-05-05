package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.Database;
import lk.ijse.pos.model.Order;
import lk.ijse.pos.views.tm.OrderTM;

import java.io.IOException;

public class OrderListFormController {


    public TableView<OrderTM> tblOrders;
    public TableColumn colOrderId;
    public TableColumn colOrderDate;
    public TableColumn colOrderCustomer;
    public TableColumn colOrderTotalCost;
    public AnchorPane contextOfOrderListForm;

    public void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("oId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrderCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colOrderTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        loadAllOrders();

        //---------------------------------------------------------
            tblOrders.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/OrderDetailsForm.fxml"));

                try {
                    Parent root = loader.load();
                    OrderDetailsFormController controller=loader.getController();
                    controller.setData(newValue.getOId());
                    Stage stage=new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } );


        //---------------------------------------------------------
    }

    private void loadAllOrders() {
        ObservableList<OrderTM> orderObList = FXCollections.observableArrayList();
        for (Order o: Database.orderList) {
            orderObList.add(new OrderTM(o.getOrderId(),o.getOrderDate(),o.getCustomerId(),o.getTotalCost()));
        }
        tblOrders.setItems(orderObList);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfOrderListForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/DashBoardForm.fxml"))));
    }
}
