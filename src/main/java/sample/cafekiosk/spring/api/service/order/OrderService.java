package sample.cafekiosk.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderCreateResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderCreateResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime){
        List<String> productNumbers = request.getProductNumbers();
        //Product
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        List<Product> duplicateProducts = findProductsBy(productNumbers, products);
        //Order
        Order order = Order.create(duplicateProducts, registeredDateTime);
        Order savedOrder = orderRepository.save(order);

        return OrderCreateResponse.of(savedOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers, List<Product> products) {
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductNumber, p -> p));


        List<Product> duplicateProducts = productNumbers.stream()
                .map(productMap::get)
                .collect(Collectors.toList());
        return duplicateProducts;
    }

}
