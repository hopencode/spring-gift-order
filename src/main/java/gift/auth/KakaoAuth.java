package gift.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Component
public class KakaoAuth {

    @Value("${REDIRECT_URL}")
    private String REDIRECT_URL;

    @Value("${REST_API_KEY}")
    private String REST_API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getKakaoLoginLink() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + REST_API_KEY
                + "&redirect_uri=" + REDIRECT_URL;
    }

    public KakaoTokenResponseDto getAccessToken(String authCode) {
        String token_url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", REDIRECT_URL);
        body.add("code", authCode);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(token_url));

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(
                request,
                KakaoTokenResponseDto.class
        );

        return response.getBody();
    }

    public String getUserEmail(String accessToken) {
        String token_url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("property_keys", "[\"kakao_account.email\"]");

        var request = new RequestEntity<>(parameters, headers, HttpMethod.POST, URI.create(token_url));

        ResponseEntity<String> response = restTemplate.exchange(
                request,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = null;
        try {
            result = mapper.readValue(response.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!result.containsKey("kakao_account")) {
            throw new RuntimeException("카카오 계정 정보(kakao_account)가 응답에 없습니다.");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) result.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        if (email == null) {
            throw new RuntimeException("카카오 계정 정보에 이메일(email) 필드가 없습니다.");
        }

        return email;
    }
}
