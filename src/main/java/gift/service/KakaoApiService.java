package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.KakaoAuth;
import gift.dto.KakaoTokenResponseDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {

    private KakaoAuth kakaoAuth;

    public KakaoApiService(KakaoAuth kakaoAuth) {
        this.kakaoAuth = kakaoAuth;
    }

    public String getKakaoLoginLink() {
        return kakaoAuth.getKakaoLoginLink();
    }

    public KakaoTokenResponseDto getAccessToken(){
        return kakaoAuth.getAccessToken();
    }

    public String getUserEmail(String accessToken) throws JsonProcessingException {
        return kakaoAuth.getUserEmail(accessToken);
    }
}
