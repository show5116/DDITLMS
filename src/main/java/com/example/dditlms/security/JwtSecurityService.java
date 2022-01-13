package com.example.dditlms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtSecurityService {
    private static final String SECRET_KEY = "ashjkdlehfaljksdhfcxbnakljshedfnjkawlkasjhdgfasjdfgasjdgfwakvbszmjhdbvlasmehfkljahsdkljfhasdkljfhalsjkdfh";
    SignatureAlgorithm signatureAlgorithm;
    byte[] secretKeyBytes;
    Key signingKey;
    public JwtSecurityService(){
        signatureAlgorithm = SignatureAlgorithm.HS512;
        secretKeyBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
    }

    public String createToken(String originToken, Long expTime) {
        if(expTime <= 0) {
            throw new RuntimeException("Expired Time must exceed zero");
        }


        return Jwts.builder()
                .setSubject(originToken)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .compact();
    }

    public String getToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
