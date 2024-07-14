package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void teardown(){
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록할 떄, 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    void createProductByProductExist(){
        //given
        Product product = createProduct("001", "아메리카노", SELLING, HANDMADE, 4000);
        productRepository.save(product);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("카페라떼")
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .price(4500)
                .build();
        //when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        //then
        assertThat(productResponse)
                .extracting("productNumber", "name", "sellingStatus", "type", "price")
                .contains("002", "카페라떼", SELLING, HANDMADE, 4500);

        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus", "type", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING, HANDMADE, 4000),
                        tuple("002", "카페라떼", SELLING, HANDMADE, 4500)
                );
    }

    @DisplayName("상품을 등록할 때, 이전에 등록한 상품이 없다면 001이다.")
    @Test
    void createProductByProductIsEmpty(){
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .name("카페라떼")
                .sellingStatus(SELLING)
                .type(HANDMADE)
                .price(4500)
                .build();
        //when
        ProductResponse productResponse = productService.createProduct(request.toServiceRequest());

        //then
        assertThat(productResponse)
                .extracting("productNumber", "name", "sellingStatus", "type", "price")
                .contains("001", "카페라떼", SELLING, HANDMADE, 4500);

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