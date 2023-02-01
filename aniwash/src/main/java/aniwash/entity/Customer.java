package aniwash.entity;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer extends User {

    @ManyToMany
    private List<Animal> animals = new ArrayList<>();

    // Use constructor for database connection
    public Customer() {
    }

    public Customer(String name, int id, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
    }
}