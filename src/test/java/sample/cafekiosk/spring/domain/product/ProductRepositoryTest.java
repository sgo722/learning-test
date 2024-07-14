package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.groups.Tuple.*;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    
    @DisplayName("현재 판매중인 상품을 조회한다.")
    @Test
    void findSellingProducts(){
        //given
        Product product1 = createProduct("001",  ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002",  ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 5000);
        Product product3 = createProduct("003",  ProductType.HANDMADE, ProductSellingStatus.STOP_SELLING, "토마토주스", 6000);

        productRepository.saveAll(
                List.of(product1, product2, product3)
        );
        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "productType", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001",  ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 4000),
                        tuple("002",  ProductType.HANDMADE, ProductSellingStatus.HOLD, "카페라떼", 5000)
                );
        
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingstatus, String name, int price) {
        return new Product(productNumber, type, sellingstatus, name, price);
    }

}