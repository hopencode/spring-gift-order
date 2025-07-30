package gift.dto;

public record KakaoTokenWithJwtResponseDto (
    KakaoTokenResponseDto kakaoTokenResponseDto,
    String jwtToken
){}