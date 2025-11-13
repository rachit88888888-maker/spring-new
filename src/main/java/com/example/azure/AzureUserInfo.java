package com.example.azure;

import java.io.Serializable;
import java.util.Map;

public class AzureUserInfo implements Serializable {

    private final String username;
    private final Map<String, Object> claims;

    public AzureUserInfo(String username, Map<String, Object> claims) {
        this.username = username;
        this.claims = claims;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }
}
