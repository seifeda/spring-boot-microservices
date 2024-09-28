package com.wegagenspringdemo.productservice.service;

import com.wegagenspringdemo.productservice.dto.ProductRequest;
import com.wegagenspringdemo.productservice.dto.ProductResponse;
import com.wegagenspringdemo.productservice.exception.ResourceNotFoundException;
import com.wegagenspringdemo.productservice.model.Product;
import com.wegagenspringdemo.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProduct() {
       List<Product> products= productRepository.findAll();
     //  return products.stream().map(product -> mapToProductResponse(product)).toList();
     return  products.stream().map(this::mapToProductResponse).toList();
    }
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id)); // Throw exception if not found
        return mapToProductResponse(product);  // Map to ProductResponse
    }

    public ProductResponse updateProductById(String id, ProductRequest productRequest) {
        // Find the product by id
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Update the product fields with the request data
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setDescription(productRequest.getDescription());
            // Add any additional fields to update here...

            // Save the updated product
            Product updatedProduct = productRepository.save(product);

            // Convert the updated Product to ProductResponse and return
            return new ProductResponse(
                    updatedProduct.getId(),
                    updatedProduct.getName(),
                    updatedProduct.getDescription(),
                    updatedProduct.getPrice()
            );
        } else {
            throw new ResourceNotFoundException("Product with id " + id + " not found.");
        }
    }

//    public void deleteProductById(String id) {
//        Optional<Product> optionalProduct = productRepository.findById(id);
//
//        if (optionalProduct.isPresent()) {
//            productRepository.deleteById(id);
//        } else {
//            throw new ResourceNotFoundException("Product with id " + id + " not found.");
//        }
//    }

    public void deleteProductById(String id) {
        // Optional: Check if the product exists before attempting deletion
        boolean exists = productRepository.existsById(id);

        if (!exists) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }

        // Delete the product by ID
        productRepository.deleteById(id);
    }
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }


}
