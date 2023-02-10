package aniwash.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    /*
       TODO: pw encryption
       @ColumnTransformer(read = "cast(AES_ENCRYPT(password, 'pwKey') as char(255))", write = "AES_DECRYPT(?, 'pwKey')")
    */
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "employees")
    private Set<Appointment> appointments = new HashSet<>();

    public Employee() {
    }

    public Employee(String username, String password, String name, String email, String title) {
        this.username = username + id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.title = title;
    }

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

}
