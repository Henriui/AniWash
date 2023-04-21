package aniwash.dao;

import aniwash.entity.Product;

import java.util.List;


public interface IProductDao extends IDao {
    boolean addProduct(Product product);

    List<Product> findAllProducts();

    Product findByIdProduct(Long id);

    Product findByNameProduct(String name);

    boolean deleteByIdProduct(Long id);

    boolean updateProduct(Product product);

    Product findNewestProduct();
}
