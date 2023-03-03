package aniwash.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Where(clause = "DELETED = 0")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @ColumnTransformer(read = "cast(AES_DECRYPT(password, 'pwKey') as char(255))", write = "AES_ENCRYPT(?, 'pwKey')")
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String title;

    @Column(name = "DELETED", nullable = false)
    private int deleted = 0;

    @ManyToMany(mappedBy = "employees")
    private Set<Appointment> appointments = new HashSet<>();

    public Employee() {
    }

    public Employee(String username, String password, String name, String email, String title) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.title = title;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.getEmployees().add(this);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
        appointment.getEmployees().remove(this);
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + ", title=" + title + ", appointments=" + appointments + ")";
    }
}
