package lk.ijse.pos.db;

import lk.ijse.pos.model.Customer;
import lk.ijse.pos.model.Item;
import lk.ijse.pos.model.Order;

import java.util.ArrayList;

public class Database {
    public static ArrayList<Customer> customersList = new ArrayList/*<>*/();
    public static ArrayList<Item> itemList = new ArrayList/*<>*/();
    public static ArrayList<Order> orderList = new ArrayList/*<>*/();

    static {
        /*customersList.add(new Customer("C-001","Nimal","Colombo",25000.00));
        customersList.add(new Customer("C-002","Sunil","Gampaha",22000.00));
*/
        //---------------Item-------------------------
        itemList.add(new Item("I-001","Description-1",70, 250.00));
        itemList.add(new Item("I-002","Description-2",50, 450.00));
    }
}
