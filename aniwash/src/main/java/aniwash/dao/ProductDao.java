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

    @Override
    public boolean addProduct(Product product) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Product p = em.find(Product.class, product.getId());
        if (em.contains(p)) {
            System.out.println("Product already exists with id: " + product.getId());
            return false;
        }
        executeInTransaction(entityManager -> em.persist(product), em);
        return true;
    }

    @Override
    public List<Product> findAllProduct() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.createQuery("SELECT p FROM Product p WHERE p.deleted = 0", Product.class).getResultList();
    }

    @Override
    public Product findByIdProduct(Long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        return em.find(Product.class, id);
    }

    @Override
    public Product findByNameProduct(String name) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
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
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Product p = em.find(Product.class, product.getId());
        if (!em.contains(p)) {
            System.out.println("Product does not exist in database. Id: " + product.getId());
            return false;
        }
        em.getTransaction().begin();
        p.setName(product.getName());
        p.setDescription(product.getDescription());
        p.setPrice(product.getPrice());
        p.setStyle(product.getStyle());
        em.getTransaction().commit();
        return true;
    }

    @Override
    public boolean deleteByIdProduct(Long id) {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Product p = em.find(Product.class, id);
        if (em.contains(p)) {
            executeInTransaction(entityManager -> em.remove(p), em);
            return true;
        }
        System.out.println("Product does not exist for id: " + id);
        return false;
    }

    @Override
    public Product findNewestProduct() {
        EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();
        Product p = null;
        try {
            p = em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No product found");
        }
        return p;
    }

    private void executeInTransaction(Consumer<EntityManager> action, EntityManager em) {
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
