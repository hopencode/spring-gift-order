package gift.dto;

import gift.entity.Product;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequestDto {

    @Size(min = 1, max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
    @Pattern(
            regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$",
            message = "문자 및 숫자 또는 유효한 특수문자 ( '( )', '[ ]', '+', '-', '&', '/', '_' ) 만 사용할 수 있습니다."
    )
    private String name;
    private Integer price;
    private String imageUrl;

    public ProductRequestDto() {}

    public ProductRequestDto(String name, Integer price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }


    public String getName() { return name; }

    public Integer getPrice() { return price; }

    public String getImageUrl() { return imageUrl; }

    public Product convertToProductEntity() {
        return new Product(this.name, this.price, this.imageUrl);
    }
}
