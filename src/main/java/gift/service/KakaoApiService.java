package gift.service;

import gift.auth.KakaoAuth;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {

    private final KakaoAuth kakaoAuth;

    private String accessToken = null;
    private String email = null;

    public KakaoApiService(KakaoAuth kakaoAuth) {
        this.kakaoAuth = kakaoAuth;
    }

    public String getKakaoLoginLink() {
        return kakaoAuth.getKakaoLoginLink();
    }

    public KakaoTokenResponseDto getAccessToken(String code) {
        KakaoTokenResponseDto responseDto = kakaoAuth.getAccessToken(code);
        accessToken = responseDto.accessToken();

        return responseDto;
    }

    public String getUserEmail(String accessToken) {
        String email = kakaoAuth.getUserEmail(this.accessToken);
        this.email = email;

        return email;
    }

    public void sendOrderMessage(String accessToken, OrderResponseDto orderResponseDto){
        kakaoAuth.sendOrderMessage(accessToken, orderResponseDto);
    }
}
