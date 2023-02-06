package aniwash.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Employee")
public class Employee extends User {
    String title;

    @OneToMany
    private Set<Appointment> appointments = new HashSet<>();

    public Employee() {
    }

    public Employee(String name, int id, String title, String phone, String email, String address, String postalcode) {
        super(name, id, phone, email, address, postalcode);
        this.title = title;
    }

    // addAppointment
    // returns true if the appointment is added
    // otherwise returns false
    public boolean addAppointment(Appointment appointment) {
        if (appointment.addEmployee(this))
            return appointments.add(appointment);
        return false;
    }

    // removeAppointment
    public boolean removeAppointment(Appointment appointment) {
        return appointments.remove(appointment);
    }
}
