package sample.cafekiosk.spring.domain.stock;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;


@SpringBootTest
class StockRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    @Test
    void findAllByProductNumberIn(){
        //given
        Stock stock1 = Stock.create("001", 1);
        Stock stock2 = Stock.create("002", 2);
        Stock stock3 = Stock.create("003", 3);


        stockRepository.saveAll(List.of(stock1, stock2, stock3));
        //when
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));
        //then
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("001",1),
                        tuple("002",2)
                );
    }



    @DisplayName("현재 재고에서 요청된 상품의 개수만큼 감소합니다.")
    @Test
    void isQuantityLessThan(){
        //given
        Stock stock = Stock.create("001", 2);
        int quantity = 3;

        //when
        boolean result = stock.isQuantityLessThan(quantity);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("재고 수량이 요청한 수량보다 적을 경우 예외가 발생합니다.")
    @Test
    void test(){
        //given
        Stock stock = Stock.create("001", 2);
        int quantity = 3;

        //when
        Assertions.assertThatThrownBy(() -> stock.deductQuantity(quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");

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