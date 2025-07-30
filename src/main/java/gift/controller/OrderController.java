package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.annotation.AuthenticatedUser;
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
                                                        @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        OrderResponseDto responseDto = orderService.createOrder(token, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
