package com.example.breakfast.rowmapper;

import com.example.breakfast.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();

        product.setProductId(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setPrice(resultSet.getInt("price"));
        product.setCreatedDate(resultSet.getTimestamp("created_date"));


        return product;
    }
}
