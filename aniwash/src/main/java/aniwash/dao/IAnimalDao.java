package aniwash.dao;

import aniwash.entity.Animal;

import java.util.List;

public interface IAnimalDao {
    boolean addAnimal(Animal animal);

    List<Animal> findAllAnimal();
    Animal findByIdAnimal(int id);

    boolean deleteByIdAnimal(int id);

    Animal updateAnimal(Animal animal);

}
