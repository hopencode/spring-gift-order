package gift.controller;

import gift.Application;
import gift.dto.ProductOptionRequestDto;
import gift.dto.ProductOptionResponseDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.service.ProductOptionService;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductOptionControllerTest {

    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    private Long savedProductId;
    private Long savedOptionId;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        DBinit();
        baseUrl = "http://localhost:" + port + "/api/products/" + savedProductId + "/options";
    }

    private void DBinit(){
        productOptionRepository.deleteAll();
        productRepository.deleteAll();

        ProductResponseDto product = productService.addProduct(
                new ProductRequestDto("초코송이", 1000, "https://choco.png")
        );

        savedProductId = product.getId();

        ProductOptionRequestDto optionDto = new ProductOptionRequestDto("기본 옵션", 100, savedProductId);
        ProductOptionResponseDto responseDto = productOptionService.addProductOption(savedProductId, optionDto);
        savedOptionId = responseDto.getId();
    }

    @Test
    void 옵션_전체_조회_테스트() {

        var response = client.get()
                .uri(baseUrl)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<ProductOptionResponseDto>>() {});

        List<ProductOptionResponseDto> options = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(options).isNotNull();
        assertThat(options).hasSize(1);
        assertThat(options.get(0).getOptionName()).isEqualTo("기본 옵션");
    }

    @Test
    void 옵션_추가_테스트() {

        ProductOptionRequestDto requestDto = new ProductOptionRequestDto("대용량", 300, savedProductId);

        var response = client.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(ProductOptionResponseDto.class);

        ProductOptionResponseDto saved = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(saved).isNotNull();
        assertThat(saved.getOptionName()).isEqualTo("대용량");
        assertThat(saved.getOptionQuantity()).isEqualTo(300);
    }

    @Test
    void 옵션_수정_테스트() {
        String url = baseUrl + "/" + savedOptionId;

        ProductOptionRequestDto updateDto = new ProductOptionRequestDto("변경된 옵션", 50, savedProductId);

        var response = client.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateDto)
                .retrieve()
                .toEntity(ProductOptionResponseDto.class);

        ProductOptionResponseDto updated = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getOptionName()).isEqualTo("변경된 옵션");
        assertThat(updated.getOptionQuantity()).isEqualTo(50);
    }

    @Test
    void 옵션_수량_차감_테스트() {
        String url = baseUrl + "/" + savedOptionId + "/subtract";

        ProductOptionRequestDto subtractDto = new ProductOptionRequestDto("기본 옵션", 20, savedProductId);

        var response = client.patch()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(subtractDto)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 옵션_삭제_테스트() {
        String url = baseUrl + "/" + savedOptionId;

        var response = client.delete()
                .uri(url)
                .retrieve()
                .toEntity(Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
