package com.mycodework.yourshops.service.order;

import com.mycodework.yourshops.dto.OrderDto;
import com.mycodework.yourshops.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrderById(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
