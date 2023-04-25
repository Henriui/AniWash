package aniwash.dao;

import aniwash.entity.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.function.Consumer;

/*
 * This class is used to access the database and perform CRUD operations on the Animal table.
 * @author rasmushy, lassib
 */
public class AnimalDao implements IAnimalDao {

    public boolean add(Animal animal) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal a = em.find(Animal.class, animal.getId());
        if (em.contains(a)) {
            System.out.println("Animal already exists with id: " + animal.getId());
            return false;
        }

        executeInTransaction(entityManager -> em.persist(animal), em);
        return true;
    }

    public List<Animal> findAll() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT a FROM Animal a WHERE a.deleted = 0", Animal.class).getResultList();
    }

    public Animal findById(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.find(Animal.class, id);
    }

    public Animal findByName(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal t = null;
        try {
            t = em.createQuery("SELECT a FROM Animal a WHERE a.name = :name", Animal.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No animal found with name: " + name);
        }
        return t;
    }

    public boolean update(Animal animal) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal t = em.find(Animal.class, animal.getId());
        if (!em.contains(t)) {
            System.out.println("Animal does not exist in database. Id: " + animal.getId());
            return false;
        }
        em.getTransaction().begin();
        t.setBreed(animal.getBreed());
        t.setType(animal.getType());
        t.setName(animal.getName());
        t.setDescription(animal.getDescription());
        em.getTransaction().commit();
        return true;
    }

    public boolean deleteById(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal a = em.find(Animal.class, id);
        if (em.contains(a)) {
            executeInTransaction(entityManager -> em.remove(a), em);
            return true;
        }
        System.out.println("Animal does not exist with id: " + id);
        return false;
    }

    public Animal findNewest() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal a = null;
        try {
            a = em.createQuery("SELECT a FROM Animal a ORDER BY a.id DESC", Animal.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No animal found");
        }
        return a;
    }

    public void executeInTransaction(Consumer<EntityManager> action, EntityManager em) {
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
