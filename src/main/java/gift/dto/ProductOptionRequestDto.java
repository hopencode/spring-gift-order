package gift.dto;

import gift.entity.ProductOption;
import jakarta.validation.constraints.*;

public class ProductOptionRequestDto {
    @NotBlank(message = "옵션 이름은 필수입니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 가능합니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
            message = "유효한 특수문자 ( '( )', '[ ]', '+', '-', '&', '/', '_' ) 가 아닙니다."
    )
    private String optionName;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 100_000_000, message = "수량은 최대 1억 개 미만이어야 합니다.")
    private Integer optionQuantity;

    private Long productId;

    public ProductOptionRequestDto() {}

    public ProductOptionRequestDto(String optionName, Integer optionQuantity, Long productId) {
        this.optionName = optionName;
        this.optionQuantity = optionQuantity;
        this.productId = productId;
    }

    public String getOptionName() {
        return optionName;
    }

    public Integer getOptionQuantity() {
        return optionQuantity;
    }

    public Long getProductId() {
        return productId;
    }
}
