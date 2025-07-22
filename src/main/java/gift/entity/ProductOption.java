package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_option")
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String optionName;

    @Column(name = "quantity", nullable = false)
    private int optionQuantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    protected ProductOption() {}

    public ProductOption(Long id, Product product, String optionName, int optionQuantity) {
        this.id = id;
        this.product = product;
        this.optionName = optionName;
        this.optionQuantity = optionQuantity;
    }

    public ProductOption(Product product, String name, int quantity) {
        this(null, product, name, quantity);
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

    public Product getProduct() {
        return product;
    }

    public void subOptionQuantity(int num){
        if (num <= 0) {
            throw new IllegalArgumentException("차감 수량은 1 이상이어야 합니다.");
        }
        if (num > this.optionQuantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.optionQuantity -= num;
    }
}
