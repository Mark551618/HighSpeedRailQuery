package com.example.breakfast.rowmapper;

import com.example.breakfast.model.OrderMain;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMainRowMapper implements RowMapper<OrderMain> {
    @Override
    public OrderMain mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderMain orderMain = new OrderMain();
        orderMain.setOrderId(rs.getInt("order_id"));
        orderMain.setTableNumber(rs.getString("table_number"));
        orderMain.setTotalPrice(rs.getInt("total_price"));
        orderMain.setCreatedTime(rs.getTimestamp("created_time"));
        return orderMain;
    }
}
