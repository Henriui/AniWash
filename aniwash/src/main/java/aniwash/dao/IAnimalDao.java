package aniwash.dao;

import aniwash.entity.Animal;

import java.util.List;

public interface IAnimalDao {
    boolean addAnimal(Animal animal);

    List<Animal> findAllAnimal();

    Animal findByIdAnimal(int id);

    Animal findByNameAnimal(String name);

    boolean deleteByIdAnimal(int id);

    boolean updateAnimal(Animal animal);

}
