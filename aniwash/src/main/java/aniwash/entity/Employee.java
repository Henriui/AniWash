package aniwash.entity;

import jakarta.persistence.*;

@Entity
public class Employee extends User {
    public Employee() {
    }

    public Employee(String name, int id, String phone, String email) {
        super(name, id, phone, email);
    }
}
