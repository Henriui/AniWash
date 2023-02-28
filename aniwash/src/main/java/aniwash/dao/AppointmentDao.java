package aniwash.dao;

import aniwash.entity.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Consumer;

/*
 * This class is used to access the database and perform CRUD operations on the Appointment table.
 * @author rasmushy, lassib
 */
public class AppointmentDao implements IAppointmentDao {

    private final EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addAppointment(Appointment appointment) {
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (app != null) {
            System.out.println("Appointment already exists with id: " + appointment.getId());
            return false;
        }
        executeInTransaction(em -> em.persist(appointment));
        return true;
    }

    @Override
    public List<Appointment> findAllAppointment() {
        return em.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();
    }

    @Override
    public Appointment findByIdAppointment(Long id) {
        return em.find(Appointment.class, id);
    }

    @Override
    public Appointment findByStartDateAppointment(ZonedDateTime date) {
        Appointment a = null;
        try {
            a = em.createQuery("SELECT a FROM Appointment a WHERE a.startDate = :date", Appointment.class).setParameter("date", date).getSingleResult();
        } catch (Exception e) {
            System.out.println("No appointment found with date: " + date);
        }
        return a;
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (app == null) {
            System.out.println("Appointment does not exist in database. Id: " + appointment.getId());
            return false;
        }
        em.getTransaction().begin();
        app.setStartDate(appointment.getStartDate());
        app.setEmployees(appointment.getEmployees());
        app.setCustomers(appointment.getCustomers());
        app.setProducts(appointment.getProducts());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdAppointment(Long id) {
        Appointment appointment = em.find(Appointment.class, id);
        if (appointment != null) {
            em.remove(appointment);
            return true;
        }
        System.out.println("No appointment found with id: " + id);
        return false;
    }

    private void executeInTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

}
