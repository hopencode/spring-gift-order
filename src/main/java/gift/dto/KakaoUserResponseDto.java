package gift.dto;

import gift.entity.KakaoUserInfo;

public record KakaoUserResponseDto(
        Long id,
        KakaoUserInfo kakaoUserInfo
) {}
