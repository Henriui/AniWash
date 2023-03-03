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

    @Override
    public boolean addAppointment(Appointment appointment) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (em.contains(app)) {
            System.out.println("Appointment already exists with id: " + appointment.getId());
            return false;
        }
        executeInTransaction(entityManager -> em.persist(appointment), em);
        return true;
    }

    @Override
    public List<Appointment> findAllAppointment() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();
    }

    @Override
    public Appointment findByIdAppointment(Long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.find(Appointment.class, id);
    }

    @Override
    public Appointment findByStartDateAppointment(ZonedDateTime date) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Appointment a = null;
        try {
            a = em.createQuery("SELECT a FROM Appointment a WHERE a.startDate = :date", Appointment.class).setParameter("startDate", date).getSingleResult();
        } catch (Exception e) {
            System.out.println("No appointment found with date: " + date);
        }
        return a;
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (!em.contains(app)) {
            System.out.println("Appointment does not exist in database. Id: " + appointment.getId());
            return false;
        }
        em.getTransaction().begin();
        app.setStartDate(appointment.getStartDate());
        app.setEndDate(appointment.getEndDate());
        app.setEmployees(appointment.getEmployees());
        app.setCustomers(appointment.getCustomers());
        app.setAnimals(appointment.getAnimals());
        app.setProducts(appointment.getProducts());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdAppointment(Long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Appointment appointment = em.find(Appointment.class, id);
        if (em.contains(appointment)) {
            executeInTransaction(entityManager -> em.remove(appointment), em);
            return true;
        }
        System.out.println("No appointment found with id: " + id);
        return false;
    }

    private void executeInTransaction(Consumer<EntityManager> action, EntityManager em) {
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
