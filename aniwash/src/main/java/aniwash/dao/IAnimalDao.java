package aniwash.dao;

import aniwash.entity.Animal;

public interface IAnimalDao extends IDao<Animal> {

    Animal findByName(String name);

}
