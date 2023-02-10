package aniwash.dao;

import aniwash.entity.Product;

import java.util.List;


public interface IProductDao {

    // Dao methods

    boolean addProduct(Product product);

    List<Product> findAllProduct();

    Product findByIdProduct(Long id);

    Product findByNameProduct(String name);

    boolean deleteByIdProduct(Long id);

    boolean updateProduct(Product product);
}
