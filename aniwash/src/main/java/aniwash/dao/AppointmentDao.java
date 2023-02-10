package aniwash.dao;

import aniwash.entity.Appointment;
import jakarta.persistence.EntityManager;

import java.util.Date;
import java.util.List;

public class AppointmentDao implements IAppointmentDao {

    EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addAppointment(Appointment appointment) {
        boolean added = true;
        em.getTransaction().begin();
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (app != null) {
            System.out.println("Appointment already exists: " + appointment.getId());
            added = false;
        } else {
            em.persist(appointment);
        }
        em.getTransaction().commit();
        return added;
    }

    @Override
    public List<Appointment> findAllAppointment() {
        em.getTransaction().begin();
        List<Appointment> appointments = em.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();
        em.getTransaction().commit();
        return appointments;
    }

    @Override
    public Appointment findByIdAppointment(Long id) {
        em.getTransaction().begin();
        Appointment appointment = em.find(Appointment.class, id);
        em.getTransaction().commit();
        return appointment;

    }

    @Override
    public Appointment findByDateAppointment(Date date) {
        Appointment app = null;
        em.getTransaction().begin();
        try {
            app = em.createQuery("SELECT a FROM Appointment a WHERE a.date = :date", Appointment.class).setParameter("date", date).getSingleResult();
        } catch (Exception e) {
            System.out.println("No appointment found with date: " + date);
        }

        em.getTransaction().commit();
        return app;
    }

    @Override
    public boolean deleteByIdAppointment(Long id) {
        em.getTransaction().begin();
        boolean deleted = false;
        Appointment appointment = em.find(Appointment.class, id);
        if (appointment != null) {
            em.remove(appointment);
            deleted = true;
        }
        em.getTransaction().commit();
        return deleted;
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        boolean updated = false;
        em.getTransaction().begin();
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (app != null) {
            app.setDate(appointment.getDate());
/*            app.setEmployee(appointment.getEmployee());
            app.setCustomer(appointment.getCustomer());
            app.setService(appointment.getService());*/
            updated = true;
        }

        em.getTransaction().commit();
        return updated;
    }
}
