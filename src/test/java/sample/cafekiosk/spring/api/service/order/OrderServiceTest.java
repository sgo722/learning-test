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
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

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
    private StockRepository stockRepository;


    @Autowired
    private OrderService orderService;

//    @AfterEach
//    void tearDown(){
//        orderRepository.deleteAllInBatch();
//        orderProductRepository.deleteAllInBatch();
//        productRepository.deleteAllInBatch();
//        stockRepository.deleteAllInBatch();
//    }

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

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct(BOTTLE, "001", 1000);
        Product product2 = createProduct(BAKERY,"002", 3000);
        Product product3 = createProduct(HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1,product2,product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);

        stockRepository.saveAll(List.of(stock1,stock2));


        //when
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001","001","002","003"))
                .build();
        OrderCreateResponse orderCreateResponse = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(orderCreateResponse.getId()).isNotNull();
        assertThat(orderCreateResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 10000);
        assertThat(orderCreateResponse.getProducts())
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", 1000),
                        tuple("001", 1000),
                        tuple("002", 3000),
                        tuple("003", 5000)
                );


        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001", 0),
                        tuple("002", 1)
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

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name("아메리카노")
                .sellingStatus(SELLING)
                .type(type)
                .price(price)
                .build();
    }

}