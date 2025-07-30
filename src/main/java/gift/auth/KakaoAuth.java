package gift.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoMessageRequestDto;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.KakaoUserResponseDto;
import gift.dto.OrderResponseDto;
import gift.entity.Link;
import gift.entity.TemplateObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Component
@ConfigurationProperties(prefix = "kakao")
public class KakaoAuth {

    private String redirectUri;

    private String restApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setRestApiKey(String restApiKey) {
        this.restApiKey = restApiKey;
    }

    public String getKakaoLoginLink() {
        return "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + restApiKey
                + "&redirect_uri=" + redirectUri
                + "&scope=account_email,talk_message";
    }

    public KakaoTokenResponseDto getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", restApiKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<KakaoTokenResponseDto> response = restTemplate.exchange(
                request,
                KakaoTokenResponseDto.class
        );

        return response.getBody();
    }

    public String getUserEmail(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("property_keys", "[\"kakao_account.email\"]");

        var request = new RequestEntity<>(parameters, headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<KakaoUserResponseDto> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                KakaoUserResponseDto.class
        );
        KakaoUserResponseDto body = response.getBody();
        if (body == null || body.kakaoUserInfo() == null || body.kakaoUserInfo().email() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자의 이메일 정보를 가져올 수 없습니다.");
        }

        return body.kakaoUserInfo().email();
    }

    public void sendOrderMessage(String accessToken, OrderResponseDto orderResponseDto) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        String text = String.format(
                "주문ID: %d\n옵션ID: %d\n수량: %d\n주문일시: %s\n메시지: %s",
                orderResponseDto.id(),
                orderResponseDto.optionId(),
                orderResponseDto.quantity(),
                orderResponseDto.orderDateTime(),
                orderResponseDto.message()
        );

        Link link = new Link("https://productWeb.com", "https://productMobileWeb.com");
        TemplateObject templateObject = new TemplateObject("text", text, link, "확인");
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("template_object", json);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
        );
    }
}
