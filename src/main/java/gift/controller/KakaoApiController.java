package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.KakaoAuth;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.ProductResponseDto;
import gift.service.KakaoApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/kakao")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;
    private String accessToken = null;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping
    public ResponseEntity<KakaoTokenResponseDto> getAccessToken() {
        KakaoTokenResponseDto responseDto = kakaoApiService.getAccessToken();
        accessToken = responseDto.getAccess_token();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail() throws JsonProcessingException {
        String email = kakaoApiService.getUserEmail(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(email != null ? email : "NO_EMAIL");
    }
}
