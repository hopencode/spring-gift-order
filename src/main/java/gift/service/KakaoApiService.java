package gift.service;

import gift.auth.KakaoAuth;
import gift.dto.KakaoTokenResponseDto;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {

    private KakaoAuth kakaoAuth;
    private String accessToken = null;

    public KakaoApiService(KakaoAuth kakaoAuth) {
        this.kakaoAuth = kakaoAuth;
    }

    public String getKakaoLoginLink() {
        return kakaoAuth.getKakaoLoginLink();
    }

    public KakaoTokenResponseDto getAccessToken(String authCode) {
        KakaoTokenResponseDto responseDto = kakaoAuth.getAccessToken(authCode);
        accessToken = responseDto.accessToken();
        System.out.println("Access token: " + accessToken);

        return responseDto;
    }

    public String getUserEmail() {
        String email = kakaoAuth.getUserEmail(this.accessToken);
        System.out.println(email);

        return email;
    }
}
