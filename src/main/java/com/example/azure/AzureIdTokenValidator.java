package com.example.azure;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

/**
 * Validates Azure AD ID tokens manually by verifying the signature against the JWKS and checking claims.
 */
@Component
public class AzureIdTokenValidator {

    private static final String JWKS_URL = "https://login.microsoftonline.com/common/discovery/keys";

    private final RemoteJWKSet<SecurityContext> remoteJWKSet;

    public AzureIdTokenValidator() {
        // RemoteJWKSet caches the keys automatically.
        this.remoteJWKSet = new RemoteJWKSet(getJwksURL(), new DefaultResourceRetriever(2000, 2000));
    }

    public AzureUserInfo validate(String idToken, String expectedIssuer, String expectedAudience) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);
            RSAKey rsaKey = selectKey(signedJWT);
            if (rsaKey == null) {
                throw new IllegalStateException("Unable to resolve signing key");
            }
            RSASSAVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
            if (!signedJWT.verify(verifier)) {
                throw new IllegalStateException("Invalid token signature");
            }

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            validateClaims(claimsSet, expectedIssuer, expectedAudience);

            Map<String, Object> claims = claimsSet.getClaims();
            String username = firstNonEmpty(claims, "preferred_username", "upn", "email", "unique_name", "sub");
            if (username == null) {
                throw new IllegalStateException("No username claim found");
            }
            return new AzureUserInfo(username, claims);
        } catch (ParseException e) {
            throw new IllegalStateException("Token parsing failed", e);
        } catch (JOSEException e) {
            throw new IllegalStateException("Signature verification failed", e);
        }
    }

    private void validateClaims(JWTClaimsSet claimsSet, String expectedIssuer, String expectedAudience) throws ParseException {
        if (!expectedIssuer.equals(claimsSet.getIssuer())) {
            throw new IllegalStateException("Unexpected issuer");
        }
        List<String> audiences = claimsSet.getAudience();
        if (audiences == null || !audiences.contains(expectedAudience)) {
            throw new IllegalStateException("Unexpected audience");
        }
        Date expiration = claimsSet.getExpirationTime();
        if (expiration == null || expiration.before(new Date())) {
            throw new IllegalStateException("Token expired");
        }
    }

    private RSAKey selectKey(SignedJWT signedJWT) throws ParseException {
        JWKSelector selector = new JWKSelector(new JWKMatcher.Builder()
                .keyID(signedJWT.getHeader().getKeyID())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .build());
        try {
            List<JWK> jwks = remoteJWKSet.get(selector, null);
            if (jwks == null || jwks.isEmpty()) {
                return null;
            }
            JWK jwk = jwks.get(0);
            if (!(jwk instanceof RSAKey)) {
                return null;
            }
            return (RSAKey) jwk;
        } catch (JOSEException e) {
            throw new IllegalStateException("JWKS retrieval failed", e);
        }
    }

    private URL getJwksURL() {
        try {
            return new URL(JWKS_URL);
        } catch (IOException e) {
            throw new IllegalStateException("Invalid JWKS URL", e);
        }
    }

    private String firstNonEmpty(Map<String, Object> claims, String... keys) {
        for (String key : keys) {
            Object value = claims.get(key);
            if (value instanceof String) {
                String str = (String) value;
                if (str != null && str.length() > 0) {
                    return str;
                }
            }
        }
        return null;
    }
}
