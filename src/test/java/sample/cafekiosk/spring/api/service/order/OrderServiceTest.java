package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;


    @DisplayName("상품번호 리스트로 주문을 생성한다.")
    @Test
    void test(){
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = createProduct("001", "아메리카노", 4000);
        Product product2 = createProduct("002", "카페라떼", 5000);
        Product product3 = createProduct("003",  "토마토주스", 6000);

        productRepository.saveAll(
                List.of(product1,product2,product3)
        );

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(
                        List.of("001", "002")
                )
                .build();
        //when
        OrderCreateResponse orderResponse = orderService.createOrder(request,registeredDateTime);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getProducts())
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002","카페라떼", 5000)
                );
    }


    private Product createProduct(String productNumber, String name, int price) {
        return new Product(productNumber, ProductType.HANDMADE, ProductSellingStatus.SELLING, name, price);
    }

}