package gift.service;

import gift.dto.ProductOptionRequestDto;
import gift.dto.ProductOptionResponseDto;
import gift.entity.Product;
import gift.entity.ProductOption;
import gift.exception.ProductOptionExceptions;
import gift.repository.ProductOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductService productService;

    public ProductOptionService(ProductOptionRepository productOptionRepository, ProductService productService) {
        this.productOptionRepository = productOptionRepository;
        this.productService = productService;
    }

    public ProductOption findProductOptionById(Long optionId) {
        return productOptionRepository.findById(optionId)
                .orElseThrow(() -> new NoSuchElementException("옵션을 찾을 수 없습니다."));
    }

    public Page<ProductOptionResponseDto> getProductOptionPageList(Long productId, Pageable pageable) {
        return productOptionRepository
                .findAllByProductId(productId, pageable)
                .map(ProductOptionResponseDto::new);
    }

    public ProductOptionResponseDto addProductOption(Long productId, ProductOptionRequestDto productOptionRequestDto) {
        Product product = productService.findById(productId);

        productOptionRepository.findByProductIdAndOptionName(productId, productOptionRequestDto.getOptionName())
                .ifPresent(option -> {
                    throw new ProductOptionExceptions.DuplicateOptionException(productId);
                });

        ProductOption productOption = productOptionRepository.save(new ProductOption(product, productOptionRequestDto.getOptionName(),
                                                                                    productOptionRequestDto.getOptionQuantity()));

        return new ProductOptionResponseDto(productOption);
    }

    public ProductOptionResponseDto updateProductOption(Long productId, Long productOptionId, ProductOptionRequestDto productOptionRequestDto) {
        Product product = productService.findById(productId);

        ProductOption productOption = findProductOptionById(productOptionId);

        if (productOption.getProduct().getId() != product.getId()) {
            throw new ProductOptionExceptions.OptionAndProductMismatchException(product.getName(), productOption.getOptionName());
        }

        productOptionRepository.findByProductIdAndOptionName(productOption.getProduct().getId(), productOptionRequestDto.getOptionName())
                .ifPresent(option -> {
                    throw new ProductOptionExceptions.DuplicateOptionException(productOption.getProduct().getId());
                });

        ProductOption updatedOption = new ProductOption(
                productOptionId,
                productOption.getProduct(),
                productOptionRequestDto.getOptionName(),
                productOptionRequestDto.getOptionQuantity()
        );

        ProductOption updatedProductOption = productOptionRepository.save(updatedOption);

        return new ProductOptionResponseDto(updatedProductOption);
    }

    @Transactional
    public void subtractProductOptionQuantity(Long productOptionId, int num) {
        ProductOption productOption = findProductOptionById(productOptionId);

        productOption.subOptionQuantity(num);
    }

    public void deleteProductOption(Long productOptionId) {
        findProductOptionById(productOptionId);

        productOptionRepository.deleteById(productOptionId);
    }
}
