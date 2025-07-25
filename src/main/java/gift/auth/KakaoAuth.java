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

    public String getUserEmail(String accessToken) throws JsonProcessingException {
        String token_url = "https://kapi.kakao.com/v2/user/me";

        System.out.println(accessToken);

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
        System.out.println("Kakao API raw response: " + response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(response.getBody(), Map.class);

        if (result.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) result.get("kakao_account");
            return (String) kakaoAccount.get("email");
        }
        return null;
    }
}
