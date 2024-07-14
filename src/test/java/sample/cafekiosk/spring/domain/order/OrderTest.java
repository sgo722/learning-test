package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.order.OrderStatus.INIT;

class OrderTest {


    @DisplayName("신규 생성한 주문의 주문 상태는 INIT이다.")
    @Test
    void saved(){
        //given
        Product product1 = createProduct("001",  ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002",  ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003",  ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "토마토주스", 6000);

        List<Product> products = List.of(product1, product2, product3);
        //when
        Order order = Order.create(products, LocalDateTime.now());

        //then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(INIT);

    }

    @DisplayName("신규 생성한 주문의 총 주문금액을 확인한다.")
    @Test
    void calculateTotalPrice(){
        //given
        Product product1 = createProduct("001",  ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002",  ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003",  ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "토마토주스", 6000);

        List<Product> products = List.of(product1, product2, product3);
        //when
        Order order = Order.create(products, LocalDateTime.now());

        //then
        assertThat(order.getTotalPrice()).isEqualTo(15000);

    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return new Product(productNumber, type, sellingStatus, name, price);
    }
}