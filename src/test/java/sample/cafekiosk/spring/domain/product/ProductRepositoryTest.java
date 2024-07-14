package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태의 상품을 조회한다.")
    @Test
    void findSellingProducts (){
        //given
        Product product1 = createProduct("001", "아메리카노", SELLING, HANDMADE, 4000);
        Product product2 = createProduct("002", "딸기케이크", HOLD, BAKERY, 5500);
        Product product3 = createProduct("003", "카페라떼", STOP_SELLING, HANDMADE, 4500);

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

    @DisplayName("상품번호 리스트에 있는 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn () {
        //given
        Product product1 = createProduct("001", "아메리카노", SELLING, HANDMADE, 4000);
        Product product2 = createProduct("002", "딸기케이크", HOLD, BAKERY, 5500);
        Product product3 = createProduct("003", "카페라떼", STOP_SELLING, HANDMADE, 4500);

        productRepository.saveAll(List.of(product1, product2, product3));
        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002", "딸기케이크", 5500)
                );

    }



    private Product createProduct(String productNumber, String name, ProductSellingStatus sellingStatus, ProductType type, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .name(name)
                .sellingStatus(sellingStatus)
                .type(type)
                .price(price)
                .build();
    }


}