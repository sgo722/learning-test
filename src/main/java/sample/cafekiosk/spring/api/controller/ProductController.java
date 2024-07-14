package sample.cafekiosk.spring.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.service.ProductService;
import sample.cafekiosk.spring.api.service.response.ProductResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/product/selling")
    public List<ProductResponse> findSellingProducts(){
        return productService.getSellingProducts();
    }


}
