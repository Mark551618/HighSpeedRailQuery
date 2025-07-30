package com.example.breakfast.service.impl;

import com.example.breakfast.dao.OrderDao;
import com.example.breakfast.dao.ProductDao;

import com.example.breakfast.model.OrderMain;
import com.example.breakfast.model.OrderItem;
import com.example.breakfast.model.Product;
import com.example.breakfast.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Integer createOrder(OrderMain orderMain, List<OrderItem> orderItemList) {
        int total = 0;

        for (OrderItem item : orderItemList) {
            // 從 DB 取回產品價格
            Product product = productDao.getProductById(item.getProductId());
            if (product == null) {
                throw new RuntimeException("找不到商品 ID: " + item.getProductId());
            }

            // 計算每筆小計
            int subtotal = product.getPrice() * item.getQuantity();
            item.setSubtotal(subtotal);  // ✅ 這裡要填上 subtotal

            total += subtotal;           // 加到總價
        }

        orderMain.setTotalPrice(total);
        orderMain.setCreatedTime(new Date());

        Integer orderId = orderDao.createOrderMain(orderMain);
        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }

    @Override
    public List<OrderMain> getAllOrderMains() {
        return orderDao.getAllOrderMains();
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderDao.getOrderItemsByOrderId(orderId);
    }
}

