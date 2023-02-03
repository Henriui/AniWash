package aniwash.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
public class Customer extends User {

    @ManyToMany
    private Set<Animal> animals = new HashSet<>();

    // Use constructor for database connection
    public Customer() {
    }

    public Customer(String name, int id, String phone, String email) {
        super(name, id, phone, email);
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "phone = " + phone + ", " +
                "email = " + email + ", " +
                "address = " + address + ", " +
                "postalcode = " + postalcode + ", " +
                "animals = " + animals + ")";
    }
}
