package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist")
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, name = "product_id")
    private Long productId;

    public WishList() {}
    public WishList(Long id, String email, Long productId) {
        this.id = id;
        this.email = email;
        this.productId = productId;
    }
    public WishList(String email, Long productId) {
        this(null, email, productId);
    }

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public Long getProductId() { return productId; }
}
