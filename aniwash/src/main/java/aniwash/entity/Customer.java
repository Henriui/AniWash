package aniwash.entity;

import aniwash.enums.UserType;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Where(clause = "DELETED = 0")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private int version;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String email;

    @Column(name = "DELETED", nullable = false)
    private int deleted = 0;

    private String address;
    private String postalCode;

    // Customer is always a customer usertype.
    private UserType userType = UserType.CUSTOMER;

    @ManyToMany(mappedBy = "owner")
    private Set<Animal> animals = new HashSet<>();

    @ManyToMany(mappedBy = "customers")
    private Set<Appointment> appointments = new HashSet<>();

    // Use constructor for database connection
    public Customer() {
    }

    public Customer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String name, String phone, String email, String address, String postalCode) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.getOwner().add(this);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.getOwner().remove(this);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.getCustomers().add(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.getCustomers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Customer))
            return false;
        Long cId = id;
        return cId.equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public List<Animal> getAnimalList() {
        return new ArrayList<>(getAnimals());
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
               "(id=" + id +
               ", name=" + name +
               ", phone=" + phone +
               ", email=" + email +
               ", address=" + address +
               ", postal code=" + postalCode +
               ", deleted=" + deleted +
               ")";
    }

}
