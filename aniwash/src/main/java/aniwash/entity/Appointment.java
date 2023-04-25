package aniwash.entity;

import aniwash.entity.localization.LocalizedAppointment;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.ZonedDateTime;
import java.util.*;

@Entity
@Where(clause = "DELETED = 0")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private int version;

    @Column(nullable = false)
    private ZonedDateTime startDate;

    @Column(nullable = false)
    private ZonedDateTime endDate;

    @Column(nullable = false)
    private long mainProductId;

    @Column(nullable = false)
    private double totalPrice;

    @Column(name = "DELETED", nullable = false)
    private int deleted = 0;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "appointment_customer", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private Set<Customer> customers = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "appointment_animal", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "animal_id"))
    private Set<Animal> animals = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "appointment_product", joinColumns = @JoinColumn(name = "appointment_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "appointment", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH }, orphanRemoval = true)
    @MapKey(name = "id")
    private Set<Discount> discounts = new HashSet<>();

    @OneToMany(mappedBy = "appointment", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH }, orphanRemoval = true)
    @MapKey(name = "localizedId.locale")
    private Map<String, LocalizedAppointment> localizations = new HashMap<>();

    public Appointment() {
    }

    public Appointment(ZonedDateTime startDate, ZonedDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void addDiscount(Discount discount) {
        discounts.add(discount);
        discount.setAppointment(this);
    }

    public void removeDiscount(Discount discount) {
        discounts.remove(discount);
        discount.setAppointment(null);
    }

    public void addProduct(Product product) {
        if (products.isEmpty()) {
            mainProductId = product.getId();
        }

        products.add(product);
        product.getAppointments().add(this);

    }

    public void addProduct(Product product, Discount discount) {
        if (products.isEmpty()) {
            mainProductId = product.getId();
        }
        addProduct(product);
        addDiscount(discount);
    }

    public void removeProduct(Product product) {
        if (product.getId() == mainProductId) {
            mainProductId = products.stream().findFirst().get().getId();
        }
        products.remove(product);
        product.getAppointments().remove(this);
    }

    public void removeProduct(Product product, Discount discount) {
        if (product.getId() == mainProductId) {
            mainProductId = products.stream().findFirst().get().getId();
        }
        products.remove(product);
        product.getAppointments().remove(this);
        discounts.remove(discount);
        discount.setAppointment(null);
    }

    public List<Customer> getCustomerList() {
        return new ArrayList<>(getCustomers());
    }

    public List<Animal> getAnimalList() {
        return new ArrayList<>(getAnimals());
    }

    public List<Product> getProductList() {
        return new ArrayList<>(getProducts());
    }

    public List<Discount> getDiscountList() {
        return new ArrayList<>(getDiscounts());
    }

    public Map<String, LocalizedAppointment> getLocalizations() {
        return localizations;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public long getMainProductId() {
        return mainProductId;
    }

    public void setMainProductId(long mainProductId) {
        this.mainProductId = mainProductId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getVersions() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDescription(String locale) {
        return localizations.get(locale).getDescription();
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public int isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Discount getDiscount(Product p) {
        return discounts.stream().filter(d -> d.getProductId() == p.getId()).findFirst().get();
    }

    public Discount getDiscount(long productId) {
        return discounts.stream().filter(d -> d.getProductId() == productId).findFirst().get();
    }

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

}
