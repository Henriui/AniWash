package aniwash.entity;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer extends User {

    @ManyToMany
    private Set<Animal> animals = new HashSet<>();

    // Use constructor for database connection
    public Customer() {
    }

    public Customer(String name, int id, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
    }

    // addAnimal
    // returns true if the animal is added
    // otherwise returns false
    public boolean addAnimal(Animal animal) {
        if (animal.addOwner(this))
            return animals.add(animal);
        return false;
    }

    // removeAnimal
    public boolean removeAnimal(Animal animal) {
        return animals.remove(animal);
    }

    // getAnimals
    public Set<Animal> getAnimals() {
        return animals;
    }
}
