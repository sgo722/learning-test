package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {


    @DisplayName("상품 타입이 재고와 관련 타입인지 확인한다.")
    @Test
    void containsStockType(){
        //given
        ProductType productType = ProductType.HANDMADE;

        //when
        boolean result = ProductType.containsType(productType);


        //then
        Assertions.assertThat(result).isFalse();
    }


    @DisplayName("상품 타입이 재고와 관련 타입인지 확인한다.")
    @Test
    void containsStockType2(){
        ProductType productType = ProductType.BOTTLE;

        //when
        boolean result = ProductType.containsType(productType);


        //then
        Assertions.assertThat(result).isTrue();
    }

    @DisplayName("상품 타입이 재고와 관련 타입인지 확인한다.")
    @Test
    void containsStockType3(){
        ProductType productType = ProductType.BAKERY;

        //when
        boolean result = ProductType.containsType(productType);


        //then
        Assertions.assertThat(result).isTrue();
    }

}