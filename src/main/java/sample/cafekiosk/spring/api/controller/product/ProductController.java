package sample.cafekiosk.spring.api.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.service.product.ProductService;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.dto.request.ProductCreateRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("/api/v1c/product/new")
    public void createProduct(@RequestBody ProductCreateRequest request){
        productService.createProduct(request);

    }
    @GetMapping("/api/v1/product/selling")
    public List<ProductResponse> getSellingProducts(){
        return productService.findAllBySellingProducts();
    }
}
