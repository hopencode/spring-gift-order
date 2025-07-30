package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.entity.KakaoUserInfo;

public record KakaoUserResponseDto(
        Long id,

        @JsonProperty("kakao_account")
        KakaoUserInfo kakaoUserInfo
) {}
