package aniwash.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type;
    private String breed;
    private int animalAge;
    private String description;

    @ManyToMany(mappedBy = "animals")
    private Set<Customer> owner = new HashSet<>();

    public Animal() {
    }

    public Animal(String name, String type, String breed, int animalAge, String description) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.animalAge = animalAge;
        this.description = description;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getBreed() {
        return breed;
    }

    public int getAnimalAge() {
        return animalAge;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAnimalAge(int animalAge) {
        this.animalAge = animalAge;
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "type = " + type + ", " +
                "breed = " + breed + ", " +
                "animalAge = " + animalAge + ", " +
                "description = " + description + ")";
    }
}
