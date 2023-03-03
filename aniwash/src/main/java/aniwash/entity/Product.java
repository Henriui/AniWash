package aniwash.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Where(clause = "DELETED = 0")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String style;

    @Column(name = "DELETED", nullable = false)
    private int deleted = 0;

    @ManyToMany(mappedBy = "products")
    private Set<Appointment> appointments = new HashSet<>();

    public Product() {
    }

    public Product(String name, String description, double price, String style) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.style = style;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.getProducts().add(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.getProducts().remove(this);
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "(id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", price=" + price +
                ", style=" + style +
                ", deleted=" + deleted +
                ")";
    }
}
