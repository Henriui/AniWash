package aniwash.entity;

import jakarta.persistence.*;
@Entity
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double discountPercent;

    @Column(nullable = false)
    private long productId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Appointment appointment;

    public Discount(long productId, double amount) {
        this.productId = productId;
        this.discountPercent = amount;
    }

    public Discount() {

    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public long getProductId() {
        return productId;
    }

    public void setDiscount(double amount) {
        this.discountPercent = amount;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Discount getDiscount() {
        return this;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

}
