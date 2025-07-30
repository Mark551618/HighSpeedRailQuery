package com.example.breakfast.service;

import com.example.breakfast.dto.OrderResponse;
import com.example.breakfast.model.OrderMain;
import com.example.breakfast.model.OrderItem;

import java.util.List;

public interface OrderService {

    Integer createOrder(OrderMain orderMain, List<OrderItem> orderItemList);

    List<OrderMain> getAllOrderMains();

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
}
