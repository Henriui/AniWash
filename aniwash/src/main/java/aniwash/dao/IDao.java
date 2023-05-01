package aniwash.dao;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.function.Consumer;
public interface IDao<T> {

    List<T> findAll();
    T findById(long id);
    T findNewest();
    boolean deleteById(long id);
    boolean add(T t);
    boolean update(T t);
    void executeInTransaction(Consumer<EntityManager> action, EntityManager em);

}
