package aniwash.dao;

import aniwash.entity.Animal;

import java.util.List;

public interface IAnimalDao extends IDao {
    boolean addAnimal(Animal animal);

    List<Animal> findAllAnimals();

    Animal findByIdAnimal(long id);

    Animal findByNameAnimal(String name);

    boolean deleteByIdAnimal(long id);

    boolean updateAnimal(Animal animal);

    Animal findNewestAnimal();
}
