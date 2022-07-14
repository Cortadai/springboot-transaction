package com.springboot.transaction.service;

import com.springboot.transaction.dto.OrderRequest;
import com.springboot.transaction.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest orderRequest);

}
