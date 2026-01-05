package com.nnt.microservices.order.service;

import com.nnt.microservices.order.dto.OrderRequest;
import com.nnt.microservices.order.model.Order;
import com.nnt.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkucCode(orderRequest.skuCode());

        orderRepository.save(order);

    }
}
