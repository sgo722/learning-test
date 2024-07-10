package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
//@Transactional
//@DataJpaTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;


    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown(){
        orderRepository.deleteAllInBatch();
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 3000);
        Product product3 = createProduct("003", 5000);
        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","002"))
                .build();
        OrderCreateResponse orderCreateResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderCreateResponse.getId()).isNotNull();
        assertThat(orderCreateResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 4000);
        assertThat(orderCreateResponse.getProducts())
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("002", 3000)
                );

    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", 1000);
        Product product2 = createProduct("002", 3000);
        Product product3 = createProduct("003", 5000);
        productRepository.saveAll(List.of(product1,product2,product3));
        //when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001"))
                .build();
        OrderCreateResponse orderCreateResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderCreateResponse.getId()).isNotNull();
        assertThat(orderCreateResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 2000);
        assertThat(orderCreateResponse.getProducts())
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000)
                );

    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name("아메리카노")
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .price(price)
                .build();
    }

}