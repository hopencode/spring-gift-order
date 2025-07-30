package gift.controller;

import gift.annotation.AccessTokenFromJwtToken;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
                                                        @AccessTokenFromJwtToken String accessToken) {
        OrderResponseDto responseDto = orderService.createOrder(accessToken, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
