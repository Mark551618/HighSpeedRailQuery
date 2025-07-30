package com.example.breakfast.dao;

import com.example.breakfast.dto.ProductRequest;
import com.example.breakfast.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer bookId);
}
