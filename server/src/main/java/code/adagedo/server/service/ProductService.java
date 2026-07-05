package code.adagedo.server.service;

import code.adagedo.server.exceptions.IllegalQuantityException;
import code.adagedo.server.exceptions.ResourceNotFoundException;
import code.adagedo.server.models.CreateProductRequest;
import code.adagedo.server.models.CreateProductResponse;
import code.adagedo.server.models.Order;
import code.adagedo.server.models.Product;
import code.adagedo.server.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final GrpcClientService grpcClientService;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request){
        Product product = Product.builder()
                .product_id(grpcClientService.getSnowFlakeId())
                .name(request.name())
                .description(request.description())
                .quantity(request.quantity())
                .price(request.price())
                .build();

        Product newProduct =  productRepository.save(product);
        return new CreateProductResponse(
                newProduct.getProduct_id(),
                newProduct.getName(),
                newProduct.getDescription(),
                newProduct.getQuantity(),
                newProduct.getPrice()
        );
    }

    @Transactional
    public CreateProductResponse getProductDetails(long id){
        Product product = productRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("product not found"));
        return new CreateProductResponse(
                product.getProduct_id(),
                product.getName(),
                product.getDescription(),
                product.getQuantity(),
                product.getPrice()
        );
    }

    @Transactional
    public CreateProductResponse placeOrder(Order order, long product_id){
        Product product = productRepository.findById(product_id).orElseThrow(() ->new ResourceNotFoundException("product not found"));
        if(order.quantity() <= 0){
            throw new IllegalQuantityException("Cannot place order with a of " + order.quantity());
        }

        if(order.quantity() > product.getQuantity()) {
            String message = String.format(
                    "Insufficient stock for product '%s' (ID: %d). Requested: %d, Available: %d.",
                    product.getName(), product.getProduct_id(), order.quantity(), product.getQuantity()
            );
            throw new IllegalQuantityException(message);
        }

        product.setQuantity(product.getQuantity() - order.quantity());
        Product updated_product = productRepository.save(product);
        return new CreateProductResponse(
                updated_product.getProduct_id(),
                updated_product.getName(),
                updated_product.getDescription(),
                updated_product.getQuantity(),
                updated_product.getPrice()
        );
    }
}
