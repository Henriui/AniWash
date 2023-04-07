package aniwash.localization;

import aniwash.entity.Appointment;
import jakarta.persistence.*;

@Entity
public class LocalizedAppointment {

    @EmbeddedId
    private LocalizedId localizedId;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private String description;

    public LocalizedAppointment() {

    }

    public LocalizedAppointment(Appointment appointment, String description) {
        this.appointment = appointment;
        this.description = description;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public String getId() {
        return localizedId.getLocale();
    }

    public String getDescription() {
        return description;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setId(LocalizedId localizedId) {
        this.localizedId = localizedId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
