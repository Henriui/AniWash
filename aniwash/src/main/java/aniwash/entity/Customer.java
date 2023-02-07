package aniwash.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String email;
    private String address;
    private String postalcode;

    @ManyToMany
    private Set<Animal> animals = new HashSet<>();

    // Use constructor for database connection
    public Customer() {
    }

    public Customer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Set<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return cId;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.cId = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + "id = " + cId + ", " + "name = " + name + ", " + "phone = " + phone + ", " + "email = " + email + ", " + "address = " + address + ", " + "postalcode = " + postalcode + ", " + "animals = " + animals + ")";
    }
}
