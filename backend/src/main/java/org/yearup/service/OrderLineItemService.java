package org.yearup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yearup.repository.OrderLineItemsRepository;

@Service
public class OrderLineItemService {
    private OrderLineItemsRepository orderLineItemsRepository;
    @Autowired
    public OrderLineItemService(OrderLineItemsRepository orderLineItemsRepository) {
        this.orderLineItemsRepository = orderLineItemsRepository;
    }
}
