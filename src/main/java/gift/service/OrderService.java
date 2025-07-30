package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Order;

import gift.entity.Product;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductOptionService productOptionService;
    private final WishListService wishListService;

    private final KakaoApiService kakaoApiService;

    public OrderService(OrderRepository orderRepository,
                        ProductOptionService productOptionService,
                        WishListService wishListService,
                        KakaoApiService kakaoApiService) {
        this.orderRepository = orderRepository;
        this.productOptionService = productOptionService;
        this.wishListService = wishListService;
        this.kakaoApiService = kakaoApiService;
    }

    public OrderResponseDto createOrder(String token, OrderRequestDto requestDto) {
        Order order = requestDto.toOrderEntity();

        productOptionService.subtractProductOptionQuantity(order.getOptionId(), order.getQuantity());
        Product product = productOptionService.findProductOptionById(order.getOptionId()).getProduct();
        String email = kakaoApiService.getUserEmail(token);
        if (wishListService.isWishListExistsByEmailAndProductId(email, product.getId())) {
            wishListService.deleteProductFromWishList(email, product.getId());
        }

        Order savedOrder = orderRepository.save(order);
        OrderResponseDto responseDto = new OrderResponseDto(savedOrder);
        kakaoApiService.sendOrderMessage(token, responseDto);

        return responseDto;
    }
}
