package com.example.breakfast.dto;

import java.util.List;

public class OrderRequest {
    private String tableNumber;
    private List<OrderItemRequest> items;

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}

