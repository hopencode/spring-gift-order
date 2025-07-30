package gift.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long optionId;

    private int quantity;

    private LocalDateTime orderDateTime;

    private String message;

    private String email;

    protected Order() {}

    public Order(Long optionId, int quantity, LocalDateTime orderDateTime, String message, String email) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}

