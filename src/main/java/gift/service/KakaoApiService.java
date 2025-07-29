package gift.service;

import gift.auth.KakaoAuth;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {

    private final KakaoAuth kakaoAuth;


    public KakaoApiService(KakaoAuth kakaoAuth) {
        this.kakaoAuth = kakaoAuth;
    }

    public String getKakaoLoginLink() {
        return kakaoAuth.getKakaoLoginLink();
    }

    public KakaoTokenResponseDto getAccessToken(String code) {

        return kakaoAuth.getAccessToken(code);
    }

    public String getUserEmail(String accessToken) {

        return kakaoAuth.getUserEmail(accessToken);
    }

    public void sendOrderMessage(String accessToken, OrderResponseDto orderResponseDto){
        kakaoAuth.sendOrderMessage(accessToken, orderResponseDto);
    }
}
