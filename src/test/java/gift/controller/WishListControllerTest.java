package gift.controller;

import gift.Application;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.WishListProductRequestDto;
import gift.entity.WishList;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import gift.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class WishListControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private WishListRepository wishListRepository;

    // testID@pusan.ac.kr 계정 토큰
    // 테스트용 application-test.properties의 jwt key 사용
    private String testJWTToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0SURAcHVzYW4uYWMua3IiLCJlbWFpbCI6InRlc3RJREBwdXNhbi5hYy5rciJ9.RaqT3G3M3kZZ1TRMCZjxkM0St_02ibEYZHxKMR7AtqU";
    private String baseUrl;
    private Long savedProduct1Id;
    private Long savedProduct2Id;

    @BeforeEach
    void setUp() {
        DBinit();
        baseUrl = "http://localhost:" + port + "/api/wishlist";
    }

    private void DBinit(){
        wishListRepository.deleteAll();
        productOptionRepository.deleteAll();
        productRepository.deleteAll();

        ProductResponseDto saveProduct1 = productService.addProduct(new ProductRequestDto("초코송이", 1000, "https://초코송이.jpg"));
        savedProduct1Id = saveProduct1.getId();

        ProductResponseDto saveProduct2 = productService.addProduct(new ProductRequestDto("포스틱", 1500, "https://포스틱.jpg"));
        savedProduct2Id = saveProduct2.getId();

        WishList wish1 = new WishList(null, "testID@pusan.ac.kr", savedProduct1Id);
        wishListRepository.save(wish1);
    }

    @Test
    @Order(1)
    void 위시_리스트_조회_테스트(){
        System.out.println("Get WishList Product test");
        String url = baseUrl + "/all";
        var response = client.get()
                .uri(url)
                .header("Authorization", "Bearer " + testJWTToken)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        List<ProductResponseDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).isInstanceOf(List.class);

        assertThat(products).hasSize(1);

        assertThat(products.getFirst().getId()).isEqualTo(savedProduct1Id);
        assertThat(products.getFirst().getName()).isEqualTo("초코송이");
        assertThat(products.getFirst().getPrice()).isEqualTo(1000);
    }

    @Test
    @Order(2)
    void 위시_리스트_상품_추가_정상_테스트(){
        System.out.println("Add Product to WishList success test");
        WishListProductRequestDto wishListProductRequestDto = new WishListProductRequestDto(savedProduct2Id);
        var response = client.post()
                .uri(baseUrl)
                .header("Authorization", "Bearer " + testJWTToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(wishListProductRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        List<ProductResponseDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).isInstanceOf(List.class);

        assertThat(products).hasSize(2);

        assertThat(products.get(1).getId()).isEqualTo(savedProduct2Id);
        assertThat(products.get(1).getName()).isEqualTo("포스틱");
        assertThat(products.get(1).getPrice()).isEqualTo(1500);
    }

    @Test
    @Order(3)
    void 위시_리스트_상품_삭제_정상_테스트(){
        System.out.println("Delete Product to WishList success test");
        String url = baseUrl + "/" + savedProduct1Id;
        var response = client.delete()
                .uri(url)
                .header("Authorization", "Bearer " + testJWTToken)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.delete()
                                .uri(url)
                                .header("Authorization", "Bearer " + testJWTToken)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    @Order(4)
    void 위시_리스트_없는_상품_삭제_NOT_FOUND_테스트(){
        System.out.println("Delete Product to WishList NOT FOUND test");
        String url = baseUrl + "/" + "-1";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.delete()
                                .uri(url)
                                .header("Authorization", "Bearer " + testJWTToken)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }
}
