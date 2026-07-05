package code.adagedo.server.controller;

import code.adagedo.server.models.CreateProductRequest;
import code.adagedo.server.models.CreateProductResponse;
import code.adagedo.server.models.Order;
import code.adagedo.server.models.Product;
import code.adagedo.server.service.GrpcClientService;
import code.adagedo.server.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(
            @RequestBody CreateProductRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<CreateProductResponse> getProductDetail(
            @PathVariable long product_id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductDetails(product_id));
    }

    @PostMapping("/{product_id}/orders")
    public ResponseEntity<CreateProductResponse> order(
            @PathVariable("product_id") long productId, @RequestBody Order order
    ){
        return ResponseEntity.status(HttpStatus.OK).body(productService.placeOrder(order, productId));
    }

}
