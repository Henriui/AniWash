package aniwash.dao;

import aniwash.entity.Animal;

// This code is defining an interface called `IAnimalDao` that extends another interface called `IDao`
// with a type parameter of `Animal`. This means that `IAnimalDao` inherits all the methods and
// properties of `IDao` and adds its own method called `findByName` that takes a `String` parameter and
// returns an `Animal` object. This interface is likely used to define the methods that a data access
// object (DAO) for the `Animal` entity should implement.
public interface IAnimalDao extends IDao<Animal> {

    Animal findByName(String name);

}
