package aniwash.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

import java.util.HashSet;
import java.util.Set;

@Entity
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

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private UserType userType;

    @ManyToMany(mappedBy = "employees")
    private Set<Appointment> appointments = new HashSet<>();

    public Employee() {
    }

    public Employee(String username, String password, String name, String email, String title, UserType userType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.title = title;
        this.deleted = false;
        this.userType = userType;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + ", title=" + title + ", appointments=" + appointments + ")";
    }
}
