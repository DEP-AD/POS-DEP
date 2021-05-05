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
import lk.ijse.pos.model.Customer;
import lk.ijse.pos.views.tm.CustomerTM;

import javax.xml.crypto.NodeSetData;
import java.io.IOException;

public class CustomerFormController {

    public AnchorPane contextOfCustomerForm;
    public TextField txtCId;
    public TextField txtCName;
    public TextField txtCSalary;
    public TextField txtCAddress;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCusId;
    public TableColumn colCusName;
    public TableColumn colCusAddress;
    public TableColumn colCusOperate;
    public TableColumn colCusSalary;
    public Button btnSave;
    public TextField txtSearch;

    public void initialize()
    {
        colCusId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCusSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colCusOperate.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadAllCustomer();
        setCustomerId();

        //------------------------------------------
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            if(newValue!=null){
                SetData(newValue);
            }
        });
        //------------------------------------------
    }

    private void setCustomerId() {
        if(Database.customersList.size()>0){
            String tempNumber = Database.customersList.get(Database.customersList.size()-1).getId();
            String array[]= tempNumber.split("C-");
            int number = Integer.parseInt(array[1]);
            number++;

            if(number>100){
                tempNumber="C-"+number;
            }else if(number>10){
                tempNumber="C-0"+number;
            }else {
                tempNumber="C-00"+number;
            }

            txtCId.setText(tempNumber);

        }else{
            txtCId.setText("C-001");
        }

    }

    private void SetData(CustomerTM value) {
        txtCId.setText(value.getId());
        txtCName.setText(value.getName());
        txtCAddress.setText(value.getAddress());
        /*txtCSalary.setText(value.getSalary()+"");*/
        txtCSalary.setText(String.valueOf(value.getSalary()));
        btnSave.setText("Update Customer");

    }


    private void loadAllCustomer() {
        ObservableList <CustomerTM> observableList = FXCollections.observableArrayList();

        for (Customer c:Database.customersList)
        {
                Button btn = new Button("Delete");

                btn.setOnAction(e->{
                    if(Database.customersList.remove(c)){
                        loadAllCustomer();
                    }
                });

                observableList.add(new CustomerTM(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn));
        }
        tblCustomer.setItems(observableList);
    }

    public void BackToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) contextOfCustomerForm.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("../views/DashBoardForm.fxml"))));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        Customer customer1 = new Customer(
                txtCId.getText(),
                txtCName.getText(),
                txtCAddress.getText(),
                Double.parseDouble(txtCSalary.getText())
        );
        if(btnSave.getText().equals("Save Customer")){
            //save
            if(Database.customersList.add(customer1)){
                new Alert(Alert.AlertType.CONFIRMATION,"Done", ButtonType.OK).show();
                loadAllCustomer();
            }else{
                new Alert(Alert.AlertType.WARNING,"Try again",ButtonType.CLOSE).show();
            }
        }else { //update
            for (int i=0; i<Database.customersList.size();i++) {
                if(txtCId.getText().equals(Database.customersList.get(i).getId())){
                    Database.customersList.remove(i);
                    if(Database.customersList.add(customer1)){
                        new Alert(Alert.AlertType.CONFIRMATION,"Updated", ButtonType.OK).show();
                        loadAllCustomer();
                        break;
                    }else{
                        new Alert(Alert.AlertType.WARNING,"Try again",ButtonType.CLOSE).show();
                    }
                }
            }
        }
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSave.setText("Save Customer");
        txtCId.clear();
        txtCName.clear();
        txtCAddress.clear();
        txtCSalary.clear();
        setCustomerId();
    }

    String searchText="";
    public void search(KeyEvent keyEvent) {
        /**searchText=searchText+""+keyEvent.getText();
        System.out.println(searchText);*/
        System.out.println(txtSearch.getText());

        ObservableList<CustomerTM> searchTm = FXCollections.observableArrayList();

        for (Customer c1:Database.customersList
             ) {
                if(
                        c1.getId().contains(searchText) ||
                            c1.getName().contains(searchText) ||
                                c1.getAddress().contains(searchText)
                ){
                    Button btn = new Button("Delete");
                    searchTm.add(new CustomerTM(c1.getId(),c1.getName(),c1.getAddress(),c1.getSalary(),btn));
                }
        }
        tblCustomer.setItems(searchTm);
    }
}
