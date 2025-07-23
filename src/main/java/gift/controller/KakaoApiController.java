package gift.controller;

import gift.auth.KakaoAuth;
import gift.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/kakao")
@PropertySource("classpath:secure.properties")
public class KakaoApiController {

    private final KakaoAuth kakaoAuth;

    public KakaoApiController(KakaoAuth kakaoAuth) {
        this.kakaoAuth = kakaoAuth;
    }

    @GetMapping
    public KakaoTokenResponseDto getAccessToken() {
        return kakaoAuth.getAccessToken();
    }
}
