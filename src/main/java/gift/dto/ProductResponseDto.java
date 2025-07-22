package gift.dto;

import gift.entity.Product;

public class ProductResponseDto {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;

    public ProductResponseDto() {}

    public ProductResponseDto(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
