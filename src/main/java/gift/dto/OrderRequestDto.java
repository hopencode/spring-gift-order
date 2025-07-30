package gift.dto;

import gift.entity.Order;

import java.time.LocalDateTime;

public record OrderRequestDto (
        Long optionId,
        int quantity,
        String message
){
    public Order toOrderEntity(String email) {
        return new Order(
                optionId,
                quantity,
                LocalDateTime.now(),
                message,
                email
        );
    }
}
