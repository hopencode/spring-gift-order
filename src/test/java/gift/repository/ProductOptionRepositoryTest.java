package gift.repository;

import gift.entity.Product;
import gift.entity.ProductOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
public class ProductOptionRepositoryTest {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product savedProduct;

    @BeforeEach
    void setUp() {
        DBinit();
    }

    private void DBinit() {
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
        Product product = new Product(null, "상품옵션 테스트용 상품", 1000, "https://test.png");
        savedProduct = productRepository.save(product);
    }

    @Test
    void productId로_ProductOption_페이지_조회_테스트() {
        ProductOption option1 = new ProductOption(null, savedProduct, "옵션1", 10);
        ProductOption option2 = new ProductOption(null, savedProduct, "옵션2", 20);
        productOptionRepository.save(option1);
        productOptionRepository.save(option2);

        Page<ProductOption> page = productOptionRepository.findAllByProductId(savedProduct.getId(), PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).extracting(ProductOption::getOptionName)
                .containsExactlyInAnyOrder("옵션1", "옵션2");

        for (ProductOption option : page.getContent()) {
            assertThat(option.getProduct().getId()).isEqualTo(savedProduct.getId());
        }
    }

    @Test
    void productId와_optionName으로_ProductOption_정상_조회_테스트() {
        ProductOption option = new ProductOption(null, savedProduct, "확인옵션", 5);
        productOptionRepository.save(option);

        Optional<ProductOption> found = productOptionRepository.findByProductIdAndOptionName(
                savedProduct.getId(), "확인옵션");

        assertThat(found).isPresent();
        assertThat(found.get().getOptionName()).isEqualTo("확인옵션");
        assertThat(found.get().getProduct().getId()).isEqualTo(savedProduct.getId());
    }

    @Test
    void 없는_optionName_조회시_빈값_반환_테스트() {
        Optional<ProductOption> found = productOptionRepository.findByProductIdAndOptionName(
                savedProduct.getId(), "없는옵션");

        assertThat(found).isEmpty();
    }
}
