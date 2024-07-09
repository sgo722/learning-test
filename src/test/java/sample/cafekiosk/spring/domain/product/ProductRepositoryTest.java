package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태의 상품을 조회한다.")
    @Test
    void findSellingProducts (){
        //given
        Product product1 = Product.builder()
                .productNumber("001")
                .name("아메리카노")
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .price(4000)
                .build();


        Product product2 = Product.builder()
                .productNumber("002")
                .name("딸기케이크")
                .sellingStatus(HOLD)
                .type(BAKERY)
                .price(5500)
                .build();

        Product product3 = Product.builder()
                .productNumber("003")
                .name("카페라떼")
                .sellingStatus(STOP_SELLING)
                .type(HANDMADE)
                .price(4500)
                .build();

        productRepository.saveAll(List.of(product1, product2, product3));
        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002", "딸기케이크", 5500)
                );

    }
}