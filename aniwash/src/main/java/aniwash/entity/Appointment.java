package aniwash.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private ZonedDateTime date;

    private String description;

    @ManyToOne
    private Employee employee;

    @OneToMany
    private Set<Product> products = new HashSet<>();

    public Appointment() {
    }

    public Appointment(ZonedDateTime date, String description) {
        this.date = date;
        this.description = description;
    }

    // addProduct
    public boolean addProduct(Product product) {
        return products.add(product);
    }

    // removeProduct
    public void removeProduct(Product product) {
        products.remove(product);
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

}
