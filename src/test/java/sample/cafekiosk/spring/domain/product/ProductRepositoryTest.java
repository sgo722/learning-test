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
        Product product1 = createProduct("001", "아메리카노", 4000, SELLING);
        Product product2 = createProduct("002", "딸기케이크", 5500, HOLD);
        Product product3 = createProduct("003", "카페라뗴", 4500, STOP_SELLING);

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

    @DisplayName("상품번호 리스트로 상품을 조회한다.")
    @Test
    void findAllByProductNumberIn (){
        //given
        Product product1 = createProduct("001", "아메리카노", 4000, SELLING);
        Product product2 = createProduct("002", "딸기케이크", 5500, HOLD);
        Product product3 = createProduct("003", "카페라뗴", 4500, STOP_SELLING);

        productRepository.saveAll(List.of(product1, product2, product3));
        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001","002"));
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002", "딸기케이크", 5500)
                );
    }

    private Product createProduct(String productNumber, String name, int price, ProductSellingStatus status) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .sellingStatus(status)
                .type(HANDMADE)
                .price(price)
                .build();
    }


}