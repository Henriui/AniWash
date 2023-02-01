package aniwash.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Employer")
public class Employer extends User {
    public Employer() {
    }

    public Employer(String name, int id, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
    }
}