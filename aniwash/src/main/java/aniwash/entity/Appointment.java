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
    private long id;

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

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employee.getAppointments().add(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.getAppointments().remove(this);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        customer.getAppointments().add(this);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
        customer.getAppointments().remove(this);
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.getAppointments().add(this);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.getAppointments().remove(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.getAppointments().add(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.getAppointments().remove(this);
    }

    // Getters and Setters
    public long getId() {
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

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

}
