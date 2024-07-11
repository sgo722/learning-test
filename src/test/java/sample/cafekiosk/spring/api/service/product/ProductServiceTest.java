package sample.cafekiosk.spring.api.service.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.dto.request.ProductCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown(){
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 등록한 상품의 상품번호에 1 증가한 값이다.")
    @Test
    void createProduct(){
        //given
        Product product = createProduct("001", "아메리카노", 4000, SELLING);

        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();
        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("002", HANDMADE, SELLING, 5000, "카푸치노");


        List<Product> products = productRepository.findAll();

        assertThat(products)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .containsExactlyInAnyOrder(
                        tuple("001",HANDMADE, SELLING, 4000, "아메리카노"),
                        tuple("002",HANDMADE, SELLING, 5000, "카푸치노")
                );

    }

    @DisplayName("신규 상품을 등록할 때, 이전에 등록된 상품이 없다면 상품번호는 001이다.")
    @Test
    void createProductWithNoProduct(){
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                 .price(5000)
                .build();
        //when
        ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "price", "name")
                .contains("001", HANDMADE, SELLING, 5000, "카푸치노");

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