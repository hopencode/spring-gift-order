package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoTokenResponseDto {

    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("expires_in")
    private int expires_in;

    @JsonProperty("refresh_token")
    private String refresh_token;

    @JsonProperty("refresh_token_expires_in")
    private int refresh_token_expires_in;

    public KakaoTokenResponseDto() {}
    public KakaoTokenResponseDto(String token_type, String access_token, int expires_in, String refresh_token, int refresh_token_expires_in) {
        this.token_type = token_type;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }
}
