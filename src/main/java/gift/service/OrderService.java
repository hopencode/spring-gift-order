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
    private final MemberService memberService;
    private final KakaoApiService kakaoApiService;


    public OrderService(OrderRepository orderRepository,
                        ProductOptionService productOptionService,
                        WishListService wishListService,
                        KakaoApiService kakaoApiService,
                        MemberService memberService) {
        this.orderRepository = orderRepository;
        this.productOptionService = productOptionService;
        this.wishListService = wishListService;
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
    }

    public OrderResponseDto createOrder(String token, OrderRequestDto requestDto) {
        String email = kakaoApiService.getUserEmail(token);
        memberService.findByEmail(email);
        Order order = requestDto.toOrderEntity(email);

        productOptionService.subtractProductOptionQuantity(order.getOptionId(), order.getQuantity());
        Product product = productOptionService.findProductOptionById(order.getOptionId()).getProduct();

        if (wishListService.isWishListExistsByEmailAndProductId(email, product.getId())) {
            wishListService.deleteProductFromWishList(email, product.getId());
        }

        Order savedOrder = orderRepository.save(order);
        OrderResponseDto responseDto = new OrderResponseDto(savedOrder);
        kakaoApiService.sendOrderMessage(token, responseDto);

        return responseDto;
    }
}
