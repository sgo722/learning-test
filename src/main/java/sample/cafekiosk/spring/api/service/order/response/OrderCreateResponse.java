package sample.cafekiosk.spring.api.service.order.response;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderStatus;
import sample.cafekiosk.spring.domain.orderproduct.OrderProduct;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderCreateResponse {


    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> products;

    @Builder
    private OrderCreateResponse(Long id, OrderStatus orderStatus, LocalDateTime registeredDateTime, List<ProductResponse> products) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderCreateResponse of(Order order) {
        return OrderCreateResponse.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .products(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }
}
