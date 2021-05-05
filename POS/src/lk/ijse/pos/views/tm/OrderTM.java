package lk.ijse.pos.views.tm;

public class OrderTM {
    private String oId;
    private String date;
    private String customer;
    private double totalCost;


    public OrderTM() {
    }

    public OrderTM(String oId, String date, String customer, double totalCost) {
        this.oId = oId;
        this.date = date;
        this.customer = customer;
        this.totalCost = totalCost;
    }

    public String getOId() {
        return oId;
    }

    public void setOId(String oId) {
        this.oId = oId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
