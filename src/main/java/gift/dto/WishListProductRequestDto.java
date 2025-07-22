package gift.dto;

public class WishListProductRequestDto {

    private Long productId;

    public WishListProductRequestDto() {}

    public WishListProductRequestDto(Long productId) {
        this.productId =  productId;
    }

    public Long getproductId() {
        return productId;
    }
}
