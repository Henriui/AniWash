package aniwash.dao;

import aniwash.entity.Animal;
import jakarta.persistence.*;

import java.util.List;

/*
 * This class is used to access the database and perform CRUD operations on the Animal table.
 * @author rasmushy
 */
public class AnimalDao implements IAnimalDao {

    public boolean addAnimal(Animal animal) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        boolean success = false;
        em.getTransaction().begin();
        em.persist(animal);
        if (animal != null) {
            success = true;
        }
        em.getTransaction().commit();
        return success;
    }

    public List<Animal> findAllAnimal() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        em.getTransaction().begin();
        List<Animal> animals = em.createQuery("SELECT a FROM Animal a", Animal.class).getResultList();
        em.getTransaction().commit();
        return animals;
    }

    public Animal findByIdAnimal(int id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        em.getTransaction().begin();
        Animal t = em.find(Animal.class, id);
        em.getTransaction().commit();
        return t;
    }

    public boolean deleteByIdAnimal(int id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        boolean deleted = false;
        em.getTransaction().begin();
        Animal t = em.find(Animal.class, id);
        if (t != null) {
            em.remove(t);
            deleted = true;
        }
        em.getTransaction().commit();
        return deleted;
    }

    public Animal updateAnimal(Animal animal) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        em.getTransaction().begin();
        Animal t = em.find(Animal.class, animal);
        t.setAnimalAge(animal.getAnimalAge());
        t.setName(animal.getName());
        t.setDescription(animal.getDescription());
        em.getTransaction().commit();
        return t;
    }
}
