package aniwash.entity;

import jakarta.persistence.*;

@Entity
public class Employee extends User {
    String title;

    public Employee() {
    }

    public Employee(String name, int id, String title, String phone, String email) {
        super(name, id, phone, email);
        this.title = title;
    }
}
