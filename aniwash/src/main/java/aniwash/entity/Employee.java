package aniwash.entity;

import jakarta.persistence.*;

@Entity
public class Employee extends User {
    private String title;

    public Employee() {
    }

    public Employee(String name, int id, String title, String phone, String email) {
        super(name, id, phone, email);
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
