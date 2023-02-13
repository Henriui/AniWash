package aniwash.dao;

import aniwash.entity.Animal;
import aniwash.entity.Customer;

import java.util.List;

public interface IAnimalDao {
    boolean addAnimal(Animal animal);

    List<Animal> findAllAnimal();

    Animal findByIdAnimal(long id);

    Animal findByNameAnimal(String name);

    boolean deleteByIdAnimal(long id);

    boolean updateAnimal(Animal animal);

}
