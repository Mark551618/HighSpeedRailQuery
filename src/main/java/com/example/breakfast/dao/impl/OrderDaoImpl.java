package com.example.breakfast.dao.impl;

import com.example.breakfast.dao.OrderDao;
import com.example.breakfast.model.OrderMain;
import com.example.breakfast.model.OrderItem;

import com.example.breakfast.rowmapper.OrderItemRowMapper;
import com.example.breakfast.rowmapper.OrderMainRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrderMain(OrderMain orderMain) {
        String sql = "INSERT INTO order_main (table_number, total_price, created_time) " +
                "VALUES (:tableNumber, :totalPrice, :createdTime)";

        Map<String, Object> map = new HashMap<>();
        map.put("tableNumber", orderMain.getTableNumber());
        map.put("totalPrice", orderMain.getTotalPrice());
        map.put("createdTime", orderMain.getCreatedTime());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, subtotal) " +
                "VALUES (:orderId, :productId, :quantity, :subtotal)";

        List<Map<String, Object>> batchValues = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("productId", item.getProductId());
            map.put("quantity", item.getQuantity());
            map.put("subtotal", item.getSubtotal());
            batchValues.add(map);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batchValues.toArray(new Map[0]));
    }

    @Override
    public List<OrderMain> getAllOrderMains() {
        String sql = "SELECT * FROM order_main ORDER BY created_time DESC";
        return namedParameterJdbcTemplate.query(sql, new OrderMainRowMapper());
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT item_id, order_id, product_id, quantity, subtotal FROM order_item WHERE order_id = :orderId";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        return namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
    }
}



