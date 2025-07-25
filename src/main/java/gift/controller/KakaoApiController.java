package gift.controller;

import gift.service.KakaoApiService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/kakao")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @GetMapping("/login")
    public void loginToGetToken(HttpServletResponse response) throws IOException {
        String kakaoLink = kakaoApiService.getKakaoLoginLink();
        response.sendRedirect(kakaoLink);
    }

    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail() {
        String email = kakaoApiService.getUserEmail();
        return ResponseEntity.ok(email);
    }
}
