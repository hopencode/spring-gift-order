package gift.controller;

import gift.dto.KakaoTokenResponseDto;
import gift.service.KakaoApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class KakaoAuthCodeController {

    private final KakaoApiService kakaoApiService;

    public KakaoAuthCodeController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping
    public ResponseEntity<KakaoTokenResponseDto> getAccessToken(@RequestParam("code") String authCode) {
        KakaoTokenResponseDto responseDto = kakaoApiService.getAccessToken(authCode);
        return ResponseEntity.ok(responseDto);
    }
}
