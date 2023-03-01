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

    private final EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addAnimal(Animal animal) {
        Animal a = em.find(Animal.class, animal.getId());
        if (a != null) {
            System.out.println("Animal already exists with id: " + animal.getId());
            return false;
        }

        executeInTransaction(em -> em.persist(animal));
        return true;
    }

    @Override
    public List<Animal> findAllAnimal() {
        return em.createQuery("SELECT a FROM Animal a", Animal.class).getResultList();
    }


    @Override
    public Animal findByIdAnimal(long id) {
        return em.find(Animal.class, id);
    }

    @Override
    public Animal findByNameAnimal(String name) {
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
        Animal t = em.find(Animal.class, animal.getId());
        if (t == null) {
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
        Animal a = em.find(Animal.class, id);
        if (a != null) {
            executeInTransaction(em -> em.remove(a));
            return true;
        }
        System.out.println("Animal does not exist with id: " + id);
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
