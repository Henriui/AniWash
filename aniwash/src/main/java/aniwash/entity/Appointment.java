package aniwash.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    private Employee employee;

    @OneToMany
    private Set<Product> products = new HashSet<>();

    public Appointment() {
    }

    public Appointment(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    // addProduct
    public boolean addProduct(Product product) {
        return products.add(product);
    }

    // removeProduct
    public void removeProduct(Product product) {
        products.remove(product);
    }

    // addEmployee
    // returns true if the employee is added
    // otherwise returns false
    public boolean addEmployee(Employee employee) {
        if (employee.addAppointment(this)) {
            this.employee = employee;
            return true;
        }
        return false;
    }

    // removeEmployee
    public void removeEmployee(Employee employee) {
        employee.removeAppointment(this);
        this.employee = null;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Set<Product> getProducts() {
        return products;
    }

}
