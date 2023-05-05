package aniwash.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.util.*;

/**
 * This is a Java class representing an animal with properties such as name, type, breed, and
 * description, and methods for managing its owners and appointments.
 * @author Lassi, Rasmus
 */
@Entity
@Where(clause = "DELETED = 0")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private int version;

    @Column(nullable = false)
    private String name;

    @Column(name = "DELETED", nullable = false)
    private int deleted = 0;

    private String type;
    private String breed;
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "customer_animal", joinColumns = {@JoinColumn(name = "animals_id")}, inverseJoinColumns = @JoinColumn(name = "owner_id"))
    private Set<Customer> owner = new HashSet<>();

    @ManyToMany(mappedBy = "animals")
    private Set<Appointment> appointments = new HashSet<>();

    public Animal() {
    }

    public Animal(String name, String type, String breed, String description) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.description = description;
    }

 /**
  * This function removes a customer from the list of owners of an animal and removes the animal from
  * the list of animals owned by the customer.
  * 
  * @param customer The "customer" parameter is an instance of the "Customer" class, which represents a
  * customer who owns one or more animals. The method "removeOwner" removes the given customer from the
  * list of owners of the current animal instance and also removes the current animal instance from the
  * list of animals owned by
  */
    public void removeOwner(Customer customer) {
        owner.remove(customer);
        customer.getAnimals().remove(this);
    }

    public void addOwner(Customer customer) {
        owner.add(customer);
        customer.getAnimals().add(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.getAnimals().remove(this);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.getAnimals().add(this);
    }

    public List<Customer> findAllOwners() {
        return new ArrayList<>(getOwner());
    }

    public List<Appointment> findAllAppointments() {
        return new ArrayList<>(getAppointments());
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;
    }

    public Set<Customer> getOwner() {
        return owner;
    }

    public void setOwner(Set<Customer> owner) {
        this.owner = owner;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Animal))
            return false;

        Animal a = (Animal) o;

        return Objects.equals(id, a.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
               "(id=" + id +
               ", name=" + name +
               ", type=" + type +
               ", breed=" + breed +
               ", description=" + description +
               ", deleted=" + deleted +
               ")";
    }

}
