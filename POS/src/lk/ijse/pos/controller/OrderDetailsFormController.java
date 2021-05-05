package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pos.db.Database;
import lk.ijse.pos.model.ItemDetails;
import lk.ijse.pos.model.Order;

public class OrderDetailsFormController {

    public TableView<ItemDetails> tblOrderDetails;
    public TableColumn colItemCode;
    public TableColumn colQty;
    public TableColumn colUnitPrice;

    public void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

    }

    public void setData(String orderId){
        for (Order o: Database.orderList) {
            if(o.getOrderId().equals(orderId)){
                ObservableList<ItemDetails> detailsObList= FXCollections.observableArrayList(o.getItems());
                tblOrderDetails.setItems(detailsObList);
                return;
            }
        }


    }
}
