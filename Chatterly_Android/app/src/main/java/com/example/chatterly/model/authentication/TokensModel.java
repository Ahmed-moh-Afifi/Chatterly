package com.example.chatterly.model.authentication;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class TokensModel {
    private String accessToken, refreshToken;

    public TokensModel(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getAccessTokenExpiration() {
        String tokenPayload = accessToken.split("\\.")[1];
        String decodedTokenPayload = new String(
                Base64.getDecoder().decode(normalizeBase64(tokenPayload)),
                StandardCharsets.UTF_8
        );

        Gson gson = new Gson();
        TokenPayload payload = gson.fromJson(decodedTokenPayload, TokenPayload.class);

        long expirationTimeInMillis = payload.exp * 1000;
        return new Date(expirationTimeInMillis);
    }

    public String getIdFromAccessToken() {
        String tokenPayload = accessToken.split("\\.")[1];
        String decodedTokenPayload = new String(
                Base64.getDecoder().decode(normalizeBase64(tokenPayload)),
                StandardCharsets.UTF_8
        );

        Gson gson = new Gson();
        TokenPayload payload = gson.fromJson(decodedTokenPayload, TokenPayload.class);

        return payload.uid;
    }

    public boolean isAccessTokenExpired() {
        Date expirationDate = getAccessTokenExpiration();
        Date thresholdDate = new Date(expirationDate.getTime() - (5 * 60 * 1000));
        return thresholdDate.before(new Date());
    }

    private String normalizeBase64(String base64) {
        return base64.replace('-', '+').replace('_', '/');
    }

    private static class TokenPayload {
        long exp;
        String uid;
    }
}
