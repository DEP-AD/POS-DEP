package lk.ijse.pos.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.pos.db.Database;
import lk.ijse.pos.model.Customer;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.model.ItemDetails;
import lk.ijse.pos.model.Order;
import lk.ijse.pos.views.tm.CartTM;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlaceOrderFormController {
    public AnchorPane contextOfPlaceOrderForm;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtCustomerSalary;
    public TextField txtItemDescription;
    public TextField txtItemQtyOnHand;
    public TextField txtItemUnitPrice;
    public TextField txtItemQty;
    public ComboBox<String> cmbCustomerId;
    public ComboBox<String> cmbItemCode;
    public TableView<CartTM> tblCart;
    public TableColumn colItemCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public Label lblDate;
    public Label lblTime;
    public Label lblTotal;
    public Label lblOrderId;

    public void initialize(){
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setDateAndTime();
        loadAllCustomerIds();
        loadAllItemCodes();
        setOrderId();

        //-------------------------------------
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerData(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            setItemData(newValue);
        });

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tempCartTM= newValue;
        });

        //-------------------------------------

    }

    private void setOrderId() {
        if(Database.orderList.size()>0){
            String tempOid = Database.orderList.get(Database.orderList.size()-1).getOrderId();
            String array[] = tempOid.split("O-");
            int number = Integer.parseInt(array[1]);
            number++;

            if(number>100){
                tempOid="O-"+number;
            }else if(number>10){
                tempOid="O-0"+number;
            }else{
                tempOid="O-00"+number;
            }

            lblOrderId.setText(tempOid);
        }else {
            lblOrderId.setText("O-001");
        }
    }

    private void setItemData(String code) {
        for (Item i: Database.itemList) {
            if(code.equals(i.getCode())){
                txtItemDescription.setText(i.getDescription());
                txtItemQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
                txtItemUnitPrice.setText(String.valueOf(i.getUnitPrice()));
                break;
            }
        }
    }

    private void loadAllItemCodes() {
        ObservableList<String> ItemObList = FXCollections.observableArrayList();
        for (Item i:Database.itemList) {
            ItemObList.add(i.getCode());
        }
        cmbItemCode.setItems(ItemObList);
    }

    private void setCustomerData(String id) {
        for (Customer c:Database.customersList) {
            if(id.equals(c.getId())){
                txtCustomerName.setText(c.getName());
                txtCustomerAddress.setText(c.getAddress());
                txtCustomerSalary.setText(String.valueOf(c.getSalary()));
                break;
            }
        }

    }

    private void loadAllCustomerIds() {
        ObservableList<String> customerObList = FXCollections.observableArrayList();
        for (Customer c: Database.customersList
        ) {
            customerObList.add(c.getId());
        }
        cmbCustomerId.setItems(customerObList);
    }

    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String tempDate=f.format(date);
         lblDate.setText(tempDate);
        /*lblDate.setText(f.format(date));
        lblTime.setText(new SimpleDateFormat("HH:mm:ss").format(date));*/

        Thread timer = new Thread(()->{
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm:ss");
            while (true){
                try{
                    Thread.sleep(1000);//1 sec
                }catch (Exception e){
                    e.printStackTrace();
                }
                final String time=simpleDateFormat.format(new Date());
                Platform.runLater(()->{
                    lblTime.setText(time);
                });
            }
        });
        timer.start();

    }

    public void BackToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfPlaceOrderForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/DashBoardForm.fxml"))));
    }

    ObservableList<CartTM> cartObList = FXCollections.observableArrayList();

    public void AddToCart(ActionEvent actionEvent) {

        if(Integer.parseInt(txtItemQty.getText())<=Integer.parseInt(txtItemQtyOnHand.getText())){
            int qty=Integer.parseInt(txtItemQty.getText());
            double unitPrice=Double.parseDouble(txtItemUnitPrice.getText());
            double total = qty * unitPrice;
            CartTM ctm = new CartTM(
                    cmbItemCode.getValue().toString(),
                    txtItemDescription.getText(),
                    qty,
                    unitPrice,
                    total
            );


            boolean isExists = checkIsExists(ctm);
            if(isExists){
                tblCart.refresh();
            }else {
                cartObList.add(ctm);
                calculateTotalCost();
                tblCart.setItems(cartObList);
            }

        }else {
            new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
        }
    }

    private boolean checkIsExists(CartTM ctm) {
        int counter=0;
        for (CartTM temp:cartObList) {
            if(temp.getItemCode().equals(ctm.getItemCode())){
                if(Integer.parseInt((txtItemQtyOnHand.getText())) >= Integer.parseInt(txtItemQty.getText())
                        +cartObList.get(counter).getQty()){

                    cartObList.get(counter).setQty(ctm.getQty()+ temp.getQty());
                    cartObList.get(counter).setTotal(ctm.getTotal()+ temp.getTotal());
                    calculateTotalCost();
                    return true;

                }else {
                    new Alert(Alert.AlertType.WARNING,"Invalid Qty").show();
                    return true;
                }
            }
            counter++;
        }
        return false;
    }

    CartTM tempCartTM=null;

    public void btnRemoveOnAction(ActionEvent actionEvent) {
        //remove row
        if(tempCartTM != null){
            for (CartTM temp:cartObList) {
                if(temp.getItemCode().equals(tempCartTM.getItemCode())){
                    cartObList.remove(temp);
                    calculateTotalCost();
                    tblCart.refresh();
                }
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select a row.").show();
        }

    }


    void calculateTotalCost(){
        double total=0.00;
        for (CartTM ctm: cartObList) {
            total+=ctm.getTotal();
        }
        lblTotal.setText(total+"/=");

    }

    public void placeOrder(ActionEvent actionEvent) {

        ArrayList<ItemDetails> details = new ArrayList<>();

        for (CartTM carttm: cartObList) {
            details.add(new ItemDetails(carttm.getItemCode(),carttm.getQty(),carttm.getUnitPrice()));
        }


        Order order=new Order(
                lblOrderId.getText(),
                lblDate.getText(),
                cmbCustomerId.getValue(),
                Double.parseDouble(lblTotal.getText().split("/=")[0]),
                details
        );

        if(Database.orderList.add(order)){
            new Alert(Alert.AlertType.CONFIRMATION,"Placed!").show();
            cartObList.clear();
            calculateTotalCost();
        }
    }
}
