package aniwash.dao;

import aniwash.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.function.Consumer;

/*
 * This class is used to access the database and perform CRUD operations on the Product table.
 * @author rasmushy, lassib
 */
public class ProductDao implements IProductDao {

    private final EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addProduct(Product product) {
        Product p = em.find(Product.class, product.getId());
        if (p != null) {
            System.out.println("Product already exists with id: " + product.getId());
            return false;
        }

        executeInTransaction(em -> em.persist(product));
        return true;
    }

    @Override
    public List<Product> findAllProduct() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    @Override
    public Product findByIdProduct(Long id) {
        return em.find(Product.class, id);
    }

    @Override
    public Product findByNameProduct(String name) {
        Product p = null;
        try {
            p = em.createQuery("SELECT p FROM Product p WHERE p.name = :name", Product.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No product found with name: " + name);
        }
        return p;
    }

    @Override
    public boolean updateProduct(Product product) {
        Product p = em.find(Product.class, product.getId());
        if (p == null) {
            System.out.println("Product does not exist in database. Id: " + product.getId());
            return false;
        }
        em.getTransaction().begin();
        p.setName(product.getName());
        p.setPrice(product.getPrice());
        p.setDescription(product.getDescription());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdProduct(Long id) {
        Product p = em.find(Product.class, id);
        if (p != null) {
            executeInTransaction(em -> em.remove(p));
            return true;
        }
        System.out.println("Product does not exist for id: " + id);
        return false;
    }

    private void executeInTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            action.accept(em);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

}
