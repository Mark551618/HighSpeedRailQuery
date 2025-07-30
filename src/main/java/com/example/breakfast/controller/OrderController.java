package com.example.breakfast.controller;


import com.example.breakfast.dto.OrderRequest;
import com.example.breakfast.dto.OrderResponse;
import com.example.breakfast.model.OrderItem;
import com.example.breakfast.model.OrderMain;
import com.example.breakfast.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public String createOrder(@RequestBody OrderRequest orderRequest) {

        // 建立 order_main 資料
        OrderMain orderMain = new OrderMain();
        orderMain.setTableNumber(orderRequest.getTableNumber());

        // 處理 order_item 資料
        List<OrderItem> orderItemList = new ArrayList<>();
        for (var itemReq : orderRequest.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.getProductId());
            item.setQuantity(itemReq.getQuantity());
            // 小計 = 後端可根據資料庫產品價格計算，或前端傳 subtotal
            // item.setSubtotal(xxx);
            orderItemList.add(item);
        }

        Integer orderId = orderService.createOrder(orderMain, orderItemList);

        return "Order created with ID: " + orderId;
    }


    @GetMapping("/main")
    public List<OrderMain> getAllOrderMains() {
        return orderService.getAllOrderMains();
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItem> getOrderItems(@PathVariable Integer orderId) {
        return orderService.getOrderItemsByOrderId(orderId);
    }
}

