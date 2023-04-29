package aniwash.dao;

import aniwash.entity.Product;

public interface IProductDao extends IDao<Product> {

    Product findByName(String name);

}
