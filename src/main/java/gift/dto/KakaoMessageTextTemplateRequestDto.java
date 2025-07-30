package gift.dto;

import gift.entity.Link;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessageTextTemplateRequestDto(
        @JsonProperty("object_type") String objectType,
        @JsonProperty("text") String text,
        @JsonProperty("link") Link link,
        @JsonProperty("button_title") String buttonTitle
) {}
