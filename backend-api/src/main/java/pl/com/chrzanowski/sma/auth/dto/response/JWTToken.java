package pl.com.chrzanowski.sma.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {
    private String tokenValue;

    public JWTToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @JsonProperty("id_token")
    public String getTokenValue() {
        return tokenValue;
    }

    void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
