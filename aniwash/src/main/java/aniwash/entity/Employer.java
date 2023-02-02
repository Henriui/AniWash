package aniwash.entity;

import jakarta.persistence.*;

@Entity
public class Employer extends User {
    public Employer() {
    }

    public Employer(String name, int id, String phone, String email) {
        super(name, id, phone, email);
    }
}
