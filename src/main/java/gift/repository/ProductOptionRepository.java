package gift.repository;

import gift.entity.ProductOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @EntityGraph(attributePaths = "product")
    Page<ProductOption> findAllByProductId(Long productId, Pageable pageable);

    @EntityGraph(attributePaths = "product")
    Optional<ProductOption> findByProductIdAndOptionName(Long productId, String optionName);
}
