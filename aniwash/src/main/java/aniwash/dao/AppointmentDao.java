package aniwash.dao;

import aniwash.datastorage.DatabaseConnector;
import aniwash.entity.Appointment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.ZonedDateTime;
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
    public ObservableList<Appointment> findAllAppointments() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> rootEntry = cq.from(Appointment.class);
        rootEntry.fetch("customers", JoinType.INNER);
        rootEntry.fetch("animals", JoinType.INNER);
        rootEntry.fetch("products", JoinType.INNER);
        cq.select(rootEntry);
        cq.where(cb.equal(rootEntry.get("deleted"), 0));
        return FXCollections.observableList(em.createQuery(cq).getResultList());
        //return em.createQuery("SELECT a FROM Appointment a", Appointment.class).getResultList();
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
            a = em.createQuery("SELECT a FROM Appointment a WHERE a.startDate = :startDate", Appointment.class).setParameter("startDate", date).getSingleResult();
        } catch (Exception e) {
            System.out.println("No appointment found with date: " + date);
        }
        return a;
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        EntityManager em = DatabaseConnector.getInstance();
        Appointment app = em.find(Appointment.class, appointment.getId());
        if (!em.contains(app)) {
            System.out.println("Appointment does not exist in database. Id: " + appointment.getId());
            return false;
        }
        try {
            em.getTransaction().begin();
            em.merge(appointment);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            throw e;
        }
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

    @Override
    public Appointment findNewestAppointment() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Appointment a = null;
        try {
            a = em.createQuery("SELECT a FROM Appointment a ORDER BY a.startDate DESC", Appointment.class).setMaxResults(1).getSingleResult();
        } catch (Exception e) {
            System.out.println("No appointment found");
        }
        return a;
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
