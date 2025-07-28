package gift.dto;

import gift.entity.KakaoUserEmail;

public record KakaoUserResponseDto(
        Long id,
        KakaoUserEmail kakao_account
) {}
