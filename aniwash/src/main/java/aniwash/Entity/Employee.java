package aniwash.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Employee")
public class Employee extends User {
    public Employee() {
    }

    public Employee(String name, int id, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
    }
}
