package gift.dto;

import gift.entity.Order;

import java.time.LocalDateTime;

public record OrderResponseDto (
        Long id,
        Long optionId,
        int quantity,
        LocalDateTime orderDateTime,
        String message
){
    public OrderResponseDto(Order order) {
        this(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }
}
