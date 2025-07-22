package gift.dto;

import gift.entity.ProductOption;

public class ProductOptionResponseDto {
    private Long id;
    private String optionName;
    private int optionQuantity;
    private Long productId;

    public ProductOptionResponseDto() {}
    public ProductOptionResponseDto(Long id, String optionName, int optionQuantity, Long productId) {
        this.id = id;
        this.optionName = optionName;
        this.optionQuantity = optionQuantity;
        this.productId = productId;
    }
    public ProductOptionResponseDto(String optionName, int optionQuantity, Long productId) {
        this(null, optionName, optionQuantity, productId);
    }

    public ProductOptionResponseDto(ProductOption productOption) {
        this.id = productOption.getId();
        this.optionName = productOption.getOptionName();
        this.optionQuantity = productOption.getOptionQuantity();
        this.productId = productOption.getProduct().getId();
    }

    public Long getId() {
        return id;
    }

    public String getOptionName() {
        return optionName;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public Long getProductId() {
        return productId;
    }
}
