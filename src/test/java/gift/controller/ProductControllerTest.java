package gift.controller;

import gift.Application;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.repository.ProductRepository;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long savedProduct1Id;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        DBinit();
        baseUrl = "http://localhost:" + port + "/api/products";
    }

    private void DBinit(){
        productRepository.deleteAll();

        ProductResponseDto saveProduct1 = productService.addProduct(new ProductRequestDto("초코송이", 1000, "https://img.danawa.com/prod_img/500000/826/577/img/3577826_1.jpg?_v=20161108161614&shrink=360:360"));
        productService.addProduct(new ProductRequestDto("포스틱", 1500, "https://m.nongshimmall.com/web/product/big/202407/b77b6109b871a7b340c5706884ef8d7a.jpg"));

        savedProduct1Id = saveProduct1.getId();
    }


    @Test
    void 상품_전체_조회_테스트() {
        System.out.println("getAll test");
        String url = baseUrl + "/all";
        var response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductResponseDto>>() {});

        List<ProductResponseDto> products = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products).isNotNull();
        assertThat(products).isInstanceOf(List.class);
        assertThat(products.get(0).getName()).isEqualTo("초코송이");
        assertThat(products.get(0).getPrice()).isEqualTo(1000);
        assertThat(products.get(0).getImageUrl()).startsWith("https://");
    }

    @Test
    void 상품_단건_조회_정상_테스트(){
        System.out.println("getProductById test");
        String url = baseUrl + "/" + savedProduct1Id;
        var response = client.get()
                .uri(url)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        ProductResponseDto product = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("초코송이");
        assertThat(product.getPrice()).isEqualTo(1000);
        assertThat(product.getImageUrl()).startsWith("https://");
    }

    @Test
    void 상품_단건_조회_없는_ID_상품_조회_시_Not_Found_테스트(){
        System.out.println("getProductById test");
        String url = baseUrl + "/-1";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.get()
                                .uri(url)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    @Test
    void 상품_추가_정상_테스트(){
        System.out.println("addProduct test");
        ProductRequestDto requestDto = new ProductRequestDto("아이스 카페 아메리카노 T", 4500, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");

        var response = client.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(requestDto.getName());
        assertThat(response.getBody().getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(response.getBody().getImageUrl()).startsWith("https://");
    }

    @Test
    void 상품_수정_정상_테스트(){
        System.out.println("updateProduct test");
        ProductRequestDto requestDto = new ProductRequestDto("아이스 카페 아메리카노 T", 5000, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        String url = baseUrl + "/" + savedProduct1Id;
        var response = client.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(requestDto.getName());
        assertThat(response.getBody().getPrice()).isEqualTo(requestDto.getPrice());
        assertThat(response.getBody().getImageUrl()).startsWith("https://");
    }

    @Test
    void 상품_삭제_정상_테스트() {
        System.out.println("deleteProduct test");

        String deleteUrl = baseUrl + "/" + savedProduct1Id;
        var deleteResponse = client.delete()
                .uri(deleteUrl)
                .retrieve()
                .toEntity(Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 삭제 후 동일 id로 다시 한번 삭제 시 Not Found 테스트
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () -> client.delete()
                                .uri(deleteUrl)
                                .retrieve()
                                .toEntity(Void.class)
                );
    }

    /*@Test
    void 승인되지_않은_카카오_이름_사용(){
        System.out.println("Not Approved Using Kakao Name test");
        ProductRequestDto requestDto = new ProductRequestDto("카카오톡", 5000, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)
                );
    }*/

    @Test
    void 승인되지_않은_특수문자_포함된_이름_사용(){
        System.out.println("Not Approved Special Character in Name test");
        ProductRequestDto requestDto = new ProductRequestDto("포스틱.", 5000, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)
                );
    }

    @Test
    void 최대_15자_길이_제한_초과_이름_사용(){
        System.out.println("Exceed Name length limit test");
        ProductRequestDto requestDto = new ProductRequestDto("포스틱포스틱포스틱포스틱포스틱포스틱", 5000, "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");

        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(baseUrl)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(ProductResponseDto.class)
                );
    }
}
