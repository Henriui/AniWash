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
    private int id;

    @Column(nullable = false)
    private ZonedDateTime date;

    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "appointment_employee", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> employees = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "appointment_customer", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "appointment_animal", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "animal_id"))
    private Set<Animal> animals = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "appointment_product", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
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
