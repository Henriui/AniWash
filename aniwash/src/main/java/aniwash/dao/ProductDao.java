package aniwash.dao;

import aniwash.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;


public class ProductDao implements IProductDao {

    private EntityManager em = aniwash.datastorage.DatabaseConnector.getInstance();

    @Override
    public boolean addProduct(Product product) {
        boolean success = true;
        em.getTransaction().begin();
        Product p = em.find(Product.class, product.getId());
        if (p != null) {
            System.out.println("Product already exists: " + product.getId());
            success = false;
        } else {
            em.persist(product);
        }
        em.getTransaction().commit();
        return success;
    }

    @Override
    public List<Product> findAllProduct() {
        em.getTransaction().begin();
        List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        em.getTransaction().commit();
        return products;
    }

    @Override
    public Product findByIdProduct(Long id) {
        em.getTransaction().begin();
        Product p = em.find(Product.class, id);
        em.getTransaction().commit();
        return p;
    }

    @Override
    public Product findByNameProduct(String name) {
        Product p = null;
        em.getTransaction().begin();
        try {
            p = em.createQuery("SELECT p FROM Product p WHERE p.name = :name", Product.class).setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("No product found with name: " + name);
        }
        em.getTransaction().commit();
        return p;
    }

    @Override
    public boolean deleteByIdProduct(Long id) {
        boolean success = true;
        em.getTransaction().begin();
        Product p = em.find(Product.class, id);
        if (p == null) {
            System.out.println("Product does not exist: " + id);
            success = false;
        } else {
            em.remove(p);
        }
        em.getTransaction().commit();
        return success;
    }

    @Override
    public boolean updateProduct(Product product) {
        boolean success = true;
        em.getTransaction().begin();
        Product p = em.find(Product.class, product.getId());
        if (p == null) {
            System.out.println("Product does not exist: " + product.getId());
            success = false;
        } else {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
            p.setDescription(product.getDescription());
        }
        em.getTransaction().commit();
        return success;
    }
}
