package gift.controller;

import gift.annotation.AuthenticatedUser;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.entity.WishList;
import gift.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }


    @GetMapping
    public Page<WishList> getWishList(
            @AuthenticatedUser String email,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return wishListService.getWishListsByEmailAndPage(email, pageable);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getWishListsByEmail(@AuthenticatedUser String email) {

        List<ProductResponseDto> products = wishListService.findAllProductsFromWishList(email);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


    @PostMapping
    public ResponseEntity<List<ProductResponseDto>> addProductToWishlist(@AuthenticatedUser String email,
                                                                         @Valid @RequestBody WishListProductRequestDto productRequestDto) {

        List<ProductResponseDto> products = wishListService.addProductToWishListByEmail(email, productRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(@AuthenticatedUser String email,
                                                          @PathVariable("productId") Long productId) {

        wishListService.deleteProductFromWishList(email, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
