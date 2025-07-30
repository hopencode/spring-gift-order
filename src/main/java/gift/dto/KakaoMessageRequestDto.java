package gift.dto;

import gift.entity.TemplateObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessageRequestDto (
        @JsonProperty("template_object") TemplateObject templateObject
) {}
