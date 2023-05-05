package aniwash.entity;

import aniwash.entity.localization.LocalizedProduct;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a Java class representing a product with price, style, and localized information, and it has
 * many-to-many and one-to-many relationships with other classes.
 */
@Entity
@Where(clause = "DELETED = 0")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private int version;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String style;

    @Column(name = "DELETED", nullable = false)
    private int deleted = 0;

    @ManyToMany(mappedBy = "products")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, orphanRemoval = true)
    @MapKey(name = "localizedId.locale")
    private Map<String, LocalizedProduct> localizations = new HashMap<>();

    public Product() {
    }

    public Product(String name, String description, double price, String style) {
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

    public String getName(String locale) {
        return localizations.get("en").getName();
    }

    public String getDescription(String locale) {
        return localizations.get("en").getDescription();
    }

    public Map<String, LocalizedProduct> getLocalizations() {
        return localizations;
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

}
