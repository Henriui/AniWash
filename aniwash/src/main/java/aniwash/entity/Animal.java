package aniwash.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    private String type;
    private String breed;
    private int animalAge;
    private String description;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "customer_animal", joinColumns = {@JoinColumn(name = "animals_id")}, inverseJoinColumns = @JoinColumn(name = "owner_id"))
    private Set<Customer> owner = new HashSet<>();

    @ManyToMany(mappedBy = "animals")
    private Set<Appointment> appointments = new HashSet<>();

    public Animal() {
    }

    public Animal(String name, String type, String breed, int animalAge, String description) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.animalAge = animalAge;
        this.description = description;
    }

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

    public int getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(int animalAge) {
        this.animalAge = animalAge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (this == o) return true;

        if (!(o instanceof Animal)) return false;

        Animal a = (Animal) o;

        return Objects.equals(id, a.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + id + ", " + "name = " + name + ", " + "type = " + type + ", " + "breed = " + breed + ", " + "animalAge = " + animalAge + ", " + "description = " + description + ")";
    }
}
