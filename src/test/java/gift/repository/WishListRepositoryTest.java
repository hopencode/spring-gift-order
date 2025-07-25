package gift.repository;

import gift.entity.WishList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Test
    void findWishListByEmail_정상_테스트() {
        WishList wish1 = new WishList(null, "findAll@email.com", 1L);
        WishList wish2 = new WishList(null, "findAll@email.com", 2L);
        WishList wish3 = new WishList(null, "findAll@email.com", 3L);

        wishListRepository.save(wish1);
        wishListRepository.save(wish2);
        wishListRepository.save(wish3);

        List<WishList> list = wishListRepository.findWishListByEmail("findAll@email.com");

        assertThat(list).hasSize(3);
        assertThat(list).allMatch(w -> w.getEmail().equals("findAll@email.com"));
    }

    @Test
    void 위시리스트에_상품_추가_정상_테스트() {
        WishList wish = new WishList(null, "add@email.com", 1L);

        WishList saved = wishListRepository.save(wish);

        List<WishList> wishLists = wishListRepository.findWishListByEmail(saved.getEmail());
        assertThat(wishLists).hasSize(1);
        assertThat(wishLists.getFirst().getEmail()).isEqualTo("add@email.com");
        assertThat(wishLists.getFirst().getProductId()).isEqualTo(1L);
    }

    @Test
    public void 위시_리스트에_상품_삭제_정상_테스트() {
        WishList wish = new WishList(null, "del@email.com", 1L);
        WishList saved = wishListRepository.save(wish);

        wishListRepository.deleteById(saved.getId());

        Optional<WishList> found = wishListRepository.findByEmailAndProductId(saved.getEmail(), saved.getProductId());
        assertThat(found).isNotPresent();
    }
}