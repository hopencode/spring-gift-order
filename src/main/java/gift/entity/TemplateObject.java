package gift.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TemplateObject(
        @JsonProperty("object_type") String objectType,
        @JsonProperty("text") String text,
        @JsonProperty("link") Link link,
        @JsonProperty("button_title") String buttonTitle
) {}
