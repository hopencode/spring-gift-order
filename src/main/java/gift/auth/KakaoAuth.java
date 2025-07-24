package gift.auth;

import gift.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@PropertySource("classpath:application.properties")
public class KakaoAuth {

    @Value("${REDIRECT_URL}")
    private String REDIRECT_URL;

    @Value("${REST_API_KEY}")
    private String REST_API_KEY;

    @Value("${AUTHORIZATION_CODE}")
    private String AUTHORIZATION_CODE;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";

    public KakaoTokenResponseDto getAccessToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", REDIRECT_URL);
        body.add("code", AUTHORIZATION_CODE);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(TOKEN_URI));

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(
                TOKEN_URI,
                HttpMethod.POST,
                request,
                KakaoTokenResponseDto.class
        );

        return response.getBody();
    }
}
