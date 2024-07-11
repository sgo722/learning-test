package sample.cafekiosk.spring.domain.product.dto.response;

import lombok.Getter;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import javax.persistence.*;

@Getter
public class ProductCreateResponse {
    private Long id;
    private String productNumber;
    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;
}
