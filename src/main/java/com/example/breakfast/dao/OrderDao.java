package com.example.breakfast.dao;

import com.example.breakfast.dto.OrderResponse;
import com.example.breakfast.model.OrderMain;
import com.example.breakfast.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer createOrderMain(OrderMain orderMain);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    List<OrderMain> getAllOrderMains();

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
}