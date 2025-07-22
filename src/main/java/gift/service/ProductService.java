package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.entity.Product;
import gift.exception.ProductExceptions;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto addProduct(ProductRequestDto requestDto) {

        String name = requestDto.getName();
        // validateUsingKakaoName(name); 추후 수정

        Product product = requestDto.convertToProductEntity();

        Product addedProduct = productRepository.save(product);

        return new ProductResponseDto(addedProduct);
    }

    public Page<Product> getProductsByPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public List<ProductResponseDto> findAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> products = new ArrayList<>();
        for (Product product : productList) {
            products.add(new ProductResponseDto(product));
        }
        return products;
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = findById(id);
        return new ProductResponseDto(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductExceptions.ProductNotFoundException(id));
    }

    public Optional<ProductResponseDto> updateProduct(Long id, ProductRequestDto requestDto) {

        String name = requestDto.getName();
        // validateUsingKakaoName(name);

        checkProductExist(id);

        Product product = new Product(
                id,
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getImageUrl()
        );

        Product updatedProduct = productRepository.save(product);
        return Optional.of(new ProductResponseDto(updatedProduct));
    }

    public void deleteProduct(Long id) {
        checkProductExist(id);

        productRepository.deleteById(id);
    }

    private void checkProductExist(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductExceptions.ProductNotFoundException(id);
        }
    }
}
