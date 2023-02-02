package aniwash.entity;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;

@Entity
public class Customer extends User {

    @ManyToMany
    private List<Animal> animals = new ArrayList<>();

    // Use constructor for database connection
    public Customer() {
    }

    public Customer(String name, int id, String phone, String email) {
        super(name, id, phone, email);
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
