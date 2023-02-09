package aniwash.dao;

import aniwash.entity.Animal;
import aniwash.entity.Customer;

import java.util.List;

public interface IAnimalDao {
    boolean addAnimal(Animal animal);

    List<Animal> findAllAnimal();

    Animal findByIdAnimal(Long id);

    Animal findByNameAnimal(String name);

    boolean deleteByIdAnimal(Long id);

    boolean updateAnimal(Animal animal);

}
