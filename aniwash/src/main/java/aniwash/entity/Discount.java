package aniwash.entity;

public class Discount {
    private Product product;
    private Appointment appointment;
    private double discount;

    public Discount(Product product, Appointment appointment, int discount) {
        this.product = product;
        this.discount = discount;
        this.appointment = appointment;
    }

    public Product getProduct() {
        return product;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getDiscount() {
        return discount;
    }
    
}
