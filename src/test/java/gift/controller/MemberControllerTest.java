package gift.controller;

import gift.Application;
import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ContextConfiguration(classes = Application.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberControllerTest {
    @LocalServerPort
    private int port;

    private RestClient client = RestClient.builder().build();

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/members";
    }

    @Test
    @Order(1)
    void 회원가입_잘못된_이메일_입력_테스트(){
        System.out.println("Member Register Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("qwertypusan.ac.kr", "12345678");
        String url = baseUrl + "/membership";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(2)
    void 회원가입_잘못된_비밀번호_입력_테스트(){
        System.out.println("Member Register Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "1234");
        String url = baseUrl + "/membership";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(3)
    void 회원가입_중복_이메일_테스트(){
        System.out.println("Elready Exist Email Register test");
        MemberRequestDto requestDto = new MemberRequestDto("abc@pusan.ac.kr", "12345678");
        String url = baseUrl + "/membership";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(4)
    void 회원가입_정상_테스트(){
        System.out.println("Member Register Success test");
        MemberRequestDto requestDto = new MemberRequestDto("zxc@pusan.ac.kr", "12345678");
        String url = baseUrl + "/membership";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Order(5)
    void 로그인_잘못된_이메일_입력_테스트(){
        System.out.println("Member Login Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("qwertypusan.ac.kr", "12345678");
        String url = baseUrl + "/login";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(6)
    void 로그인_잘못된_비밀번호_입력_테스트(){
        System.out.println("Member Login Not Valid Email test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "1234");
        String url = baseUrl + "/login";
        assertThatExceptionOfType(HttpClientErrorException.BadRequest.class)
                .isThrownBy(
                        () ->
                                client.post()
                                        .uri(url)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestDto)
                                        .retrieve()
                                        .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(7)
    void 로그인_없는_이메일_시도_테스트(){
        System.out.println("Member Login Not register Email test");
        MemberRequestDto requestDto = new MemberRequestDto("qwerty@pusan.ac.kr", "12345678");
        String url = baseUrl + "/login";
        assertThatExceptionOfType(HttpClientErrorException.NotFound.class)
                .isThrownBy(
                        () ->
                            client.post()
                                    .uri(url)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(requestDto)
                                    .retrieve()
                                    .toEntity(MemberResponseDto.class)
                        );
    }

    @Test
    @Order(8)
    void 로그인_틀린_비밀번호_테스트(){
        System.out.println("Member Login Invalid password test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "12345678888");
        String url = baseUrl + "/login";
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(
                        () -> client.post()
                                .uri(url)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(requestDto)
                                .retrieve()
                                .toEntity(MemberResponseDto.class)
                );
    }

    @Test
    @Order(9)
    void 로그인_정상_테스트(){
        System.out.println("Member Login success test");
        MemberRequestDto requestDto = new MemberRequestDto("abcd@pusan.ac.kr", "12345678");
        String url = baseUrl + "/login";
        var response = client.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .retrieve()
                .toEntity(MemberResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
