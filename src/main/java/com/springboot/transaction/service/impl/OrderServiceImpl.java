package com.springboot.transaction.service.impl;

import com.springboot.transaction.dto.OrderRequest;
import com.springboot.transaction.dto.OrderResponse;
import com.springboot.transaction.entity.Order;
import com.springboot.transaction.entity.Payment;
import com.springboot.transaction.exception.PaymentException;
import com.springboot.transaction.repository.OrderRepository;
import com.springboot.transaction.repository.PaymentRepository;
import com.springboot.transaction.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {

        // save order details
        Order order = orderRequest.getOrder();
        order.setStatus("INPROGRESS");
        order.setOrderTrackingNumber(UUID.randomUUID().toString());
        orderRepository.save(order);

        // save payment details
        Payment payment = orderRequest.getPayment();
        if(payment.getType().equals("DEBIT")){
            throw new PaymentException("Payment card type not supported");
        }
        payment.setOrderId(order.getId());
        paymentRepository.save(payment);

        // manage orderResponse
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderTrackingNumber(order.getOrderTrackingNumber());
        orderResponse.setMessage("SUCCESS");
        orderResponse.setStatus(order.getStatus());

        return orderResponse;
    }
}
