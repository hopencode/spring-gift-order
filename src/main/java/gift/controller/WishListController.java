package gift.controller;

import gift.annotation.EmailFromJwtToken;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.entity.WishList;
import gift.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
            @EmailFromJwtToken String email,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return wishListService.getWishListsByEmailAndPage(email, pageable);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getWishListsByEmail(@EmailFromJwtToken String email) {

        List<ProductResponseDto> products = wishListService.findAllProductsFromWishList(email);

        return ResponseEntity.ok(products);
    }


    @PostMapping
    public ResponseEntity<List<ProductResponseDto>> addProductToWishlist(@EmailFromJwtToken String email,
                                                                         @Valid @RequestBody WishListProductRequestDto productRequestDto) {

        List<ProductResponseDto> products = wishListService.addProductToWishListByEmail(email, productRequestDto);

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductFromWishlist(@EmailFromJwtToken String email,
                                                          @PathVariable("productId") Long productId) {

        wishListService.deleteProductFromWishList(email, productId);
        return ResponseEntity.noContent().build();
    }
}
