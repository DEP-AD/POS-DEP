package lk.ijse.pos.model;

public class Item {
        private String code;
        private String description;
        private int qtyOnHand;
        private double unitPrice;


    public Item() {//alt+insert -> Constructor

    }

    public Item(String code, String description, int qtyOnHand, double unitPrice) {//alt+insert -> Constructor
        this.code = code;
        this.description = description;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
    }

    //-------------------------- alt+ctrl+shift+t -> Encapsulation fields ---------------------------
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    //---------------------------------------------------
}
