package gift.repository;


import gift.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        DBinit();
    }

    private void DBinit() {
        productRepository.deleteAll();
    }

    @Test
    void 상품_저장_정상_테스트() {
        Product product = new Product(null, "저장테스트", 1000, "https://저장테스트.png");

        var result = productRepository.save(product);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(product.getName())
        );
    }

    @Test
    void 저장된_상품_id_검색_정상_테스트() {
        Product product = new Product(null, "검색테스트", 1000, "https://검색테스트.png");

        var saved = productRepository.save(product);

        var found = productRepository.findById(saved.getId()).get().getName();

        assertThat(found).isNotNull();
        assertThat(found).isEqualTo(product.getName());
    }

    @Test
    public void 저장된_상품_전체_목록_조회_정상_테스트() {

        Product product1 = new Product(null, "조회테스트1", 1000, "https://조회테스트1.png");
        Product product2 = new Product(null, "조회테스트2", 1500, "https://조회테스트2.png");

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
        assertThat(products).extracting("name").contains("조회테스트1", "조회테스트2");
    }

    @Test
    public void 상품_수정_정상_테스트() {
        Product product = new Product(null, "수정테스트1", 1000, "https://수정테스트2.png");
        var saved = productRepository.save(product);

        Product updateProduct = new Product(saved.getId(), "수정테스트2", 1500, "https://수정테스트2.png");
        productRepository.save(updateProduct);

        Optional<Product> updated = productRepository.findById(product.getId());

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("수정테스트2");
        assertThat(updated.get().getPrice()).isEqualTo(1500);
    }

    @Test
    public void 상품_삭제_정상_테스트() {
        Product product = new Product(null, "삭제테스트", 1000, "https://삭제테스트.png");
        var saved = productRepository.save(product);
        Long id = saved.getId();

        productRepository.deleteById(id);

        Optional<Product> deleted = productRepository.findById(id);
        assertThat(deleted).isNotPresent();
    }
}