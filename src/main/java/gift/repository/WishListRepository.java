package gift.repository;

import gift.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    Optional<WishList> findByEmailAndProductId(String email, Long productId);

    List<WishList> findWishListByEmail(String email);

    Page<WishList> findWishListByEmail(String email, Pageable pageable);
}
