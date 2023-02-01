package aniwash.entity;

import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.*;

@Entity
@Table(name = "Animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    private String type;
    private String breed;
    private String description;

    @ManyToMany(mappedBy = "animals")
    private Set<Customer> owner = new HashSet<>();

    public Animal() {
    }

    public Animal(String name, String type, String breed, String description) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.description = description;
    }

    // addOwner
    // returns true if the owner is added
    // otherwise returns false
    public boolean addOwner(Customer customer) {
        return owner.add(customer);
    }

    // removeOwner
    public void removeOwner(Customer customer) {
        owner.remove(customer);
    }

    // getOwner
    public Set<Customer> getOwner() {
        return owner;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getBreed() {
        return breed;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
