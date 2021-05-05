package lk.ijse.pos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.Database;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.views.tm.ItemTM;

import java.io.IOException;
import java.io.StringReader;

public class ItemFormController {

    public AnchorPane contextOfItemForm;
    public TextField txtItemCode;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public Button btnSaveButton;
    public TableView<ItemTM> tblItem;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;
    public TableColumn colUnitPrice;
    public TableColumn colOperate;
    public TextField txtSearch;


    ObservableList<ItemTM> obList= FXCollections.observableArrayList();

    public void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadItems("");
        setItemCode();

        //---------------------------------------------------
        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                setData(newValue);
            }
        });

    }

    private void setItemCode() {
        if(Database.itemList.size()>0){
            String tempCode=Database.itemList.get(Database.itemList.size()-1).getCode();
            String array[] =tempCode.split("I-");
            int code=Integer.parseInt(array[1]);
            code++;

            if(code>100){
                tempCode="I-"+code;
            }else if(code>10){
                tempCode="I-0"+code;
            }else {
                tempCode="I-00"+code;
            }

            txtItemCode.setText(tempCode);
        }else{
            txtItemCode.setText("I-001");
        }
    }

    private void setData(ItemTM itm) {
        txtItemCode.setText(itm.getCode());
        txtDescription.setText(itm.getDescription());
        txtQtyOnHand.setText(String.valueOf(itm.getQtyOnHand()));
        txtUnitPrice.setText(String.valueOf(itm.getUnitPrice()));
        btnSaveButton.setText("Update Item");
    }


    private void loadItems(String searchText) {
        obList.clear();
        for (Item i:Database.itemList) {
            Button btn = new Button("Delete");
            if(i.getCode().contains(searchText)|| i.getDescription().contains(searchText)) {
                obList.add(new ItemTM(i.getCode(), i.getDescription(), i.getQtyOnHand(), i.getUnitPrice(), btn));

                //-------------Delete Button-------------
                btn.setOnAction(e-> {
                    if(Database.itemList.remove(i)){
                        new Alert(Alert.AlertType.CONFIRMATION,"Deleted").show();
                        loadItems("");
                    }else {
                        new Alert(Alert.AlertType.WARNING,"Try again").show();
                    }
                } );
                //---------------------------------------
            }
        }
        tblItem.setItems(obList);
    }


    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfItemForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/DashBoardForm.fxml"))));
    }

    public void SaveItemOnAction(ActionEvent actionEvent) {
        Item item1 = new Item(
                txtItemCode.getText(),
                txtDescription.getText(),
                Integer.parseInt(txtQtyOnHand.getText()),
                Double.parseDouble(txtUnitPrice.getText())
        );

        if(btnSaveButton.getText().equals("Save Item")){
            //Save
            if(Database.itemList.add(item1)){
                new Alert(Alert.AlertType.CONFIRMATION,"Saved", ButtonType.OK).show();
                loadItems("");
            }else{
                new Alert(Alert.AlertType.WARNING,"Try Again", ButtonType.CLOSE).show();
            }


        }else{
            //Update
            int counter=0;
            for (Item i:Database.itemList) {
                if(txtItemCode.getText().equals(i.getCode())){
                    Database.itemList.get(counter).setDescription(txtDescription.getText());
                    Database.itemList.get(counter).setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                    Database.itemList.get(counter).setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                    loadItems("");
                    break;
                }
                counter++;
            }
        }
    }

    public void NewItemOnAction(ActionEvent actionEvent) {
        txtItemCode.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        btnSaveButton.setText("Save Item");
        setItemCode();
    }

    //String tempSearchText="";
    public void searchItem(KeyEvent keyEvent) {
        //tempSearchText=tempSearchText+""+keyEvent.getText();
        loadItems(txtSearch.getText());

    }
}
