package aniwash.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Employee")
public class Employee extends User {
    String title;

    public Employee() {
    }

    public Employee(String name, int id, String title, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
        this.title = title;
    }
}
