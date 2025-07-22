package gift.service;

import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishList;
import gift.repository.WishListRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public WishListService(WishListRepository wishListRepository,
                           MemberService memberService,
                           ProductService productService) {
        this.wishListRepository = wishListRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    public Page<WishList> getWishListsByEmailAndPage(String email, Pageable pageable) {
        return wishListRepository.findWishListByEmail(email, pageable);
    }

    public List<ProductResponseDto> findAllProductsFromWishList(String email) {
        validateMemberExists(email);
        List<WishList> wishLists = wishListRepository.findWishListByEmail(email);

        return wishLists.stream()
                .map(WishList::getProductId)
                .map(productService::findById)
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> addProductToWishListByEmail(String email, WishListProductRequestDto requestDto) {
        validateMemberExists(email);
        Long productId = requestDto.getproductId();
        Product product = productService.findById(productId);
        WishList wish = new WishList(email, productId);
        wishListRepository.save(wish);

        return findAllProductsFromWishList(email);
    }

    public void deleteProductFromWishList(String email, Long productId) {
        validateMemberExists(email);
        WishList wishList = wishListRepository.findByEmailAndProductId(email, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "찜 목록에 해당 상품이 없습니다."));

        wishListRepository.deleteById(wishList.getId());
    }

    private void validateMemberExists(String email) {
        Member member = memberService.findByEmail(email);
    }
}
