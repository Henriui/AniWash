package aniwash.dao;

import aniwash.entity.Animal;
import aniwash.entity.Customer;
import jakarta.persistence.*;

import java.util.List;

/*
 * This class is used to access the database and perform CRUD operations on the Animal table.
 * @author rasmushy
 */
public class AnimalDao implements IAnimalDao {
    private EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addAnimal(Animal animal) {
        boolean success = true;
        em.getTransaction().begin();
        Animal a = em.find(Animal.class, animal.getId());
        if (a != null) {
            System.out.println("Animal already exists: " + animal.getId());
            success = false;
        } else {
            em.persist(animal);
        }
        em.getTransaction().commit();
        return success;
    }

    @Override
    public List<Animal> findAllAnimal() {
        em.getTransaction().begin();
        List<Animal> animals = em.createQuery("SELECT a FROM Animal a", Animal.class).getResultList();
        em.getTransaction().commit();
        return animals;
    }


    @Override
    public Animal findByIdAnimal(Long id) {
        em.getTransaction().begin();
        Animal t = em.find(Animal.class, id);
        em.getTransaction().commit();
        return t;
    }

    @Override
    public Animal findByNameAnimal(String name) {
        Animal t = null;
        em.getTransaction().begin();
        try {
            t = em.createQuery("SELECT a FROM Animal a WHERE a.name = :name", Animal.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No animal found with name: " + name);
        }
        em.getTransaction().commit();
        return t;
    }

    @Override
    public boolean deleteByIdAnimal(Long id) {
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

    @Override
    public boolean updateAnimal(Animal animal) {
        em.getTransaction().begin();
        Animal t = em.find(Animal.class, animal.getId());
        if (t == null) {
            em.getTransaction().commit();
            return false;
        }
        t.setBreed(animal.getBreed());
        t.setType(animal.getType());
        t.setAnimalAge(animal.getAnimalAge());
        t.setName(animal.getName());
        t.setDescription(animal.getDescription());
        em.getTransaction().commit();
        return true;
    }
}
