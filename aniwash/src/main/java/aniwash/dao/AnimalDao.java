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

    @Override
    public boolean addAnimal(Animal animal) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal a = em.find(Animal.class, animal.getId());
        if (em.contains(a)) {
            System.out.println("Animal already exists with id: " + animal.getId());
            return false;
        }

        executeInTransaction(entityManager -> em.persist(animal), em);
        return true;
    }

    @Override
    public List<Animal> findAllAnimal() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT a FROM Animal a", Animal.class).getResultList();
    }


    @Override
    public Animal findByIdAnimal(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.find(Animal.class, id);
    }

    @Override
    public Animal findByNameAnimal(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal t = null;
        try {
            t = em.createQuery("SELECT a FROM Animal a WHERE a.name = :name", Animal.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No animal found with name: " + name);
        }
        return t;
    }

    @Override
    public boolean updateAnimal(Animal animal) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal t = em.find(Animal.class, animal.getId());
        if (!em.contains(t)) {
            System.out.println("Animal does not exist in database. Id: " + animal.getId());
            return false;
        }
        em.getTransaction().begin();
        t.setBreed(animal.getBreed());
        t.setType(animal.getType());
        t.setAnimalAge(animal.getAnimalAge());
        t.setName(animal.getName());
        t.setDescription(animal.getDescription());
        t.setOwner(animal.getOwner());
        t.setAppointments(animal.getAppointments());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdAnimal(long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Animal a = em.find(Animal.class, id);
        if (em.contains(a)) {
            executeInTransaction(entityManager -> em.remove(a), em);
            return true;
        }
        System.out.println("Animal does not exist with id: " + id);
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
