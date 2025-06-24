package com.soopgyeol.api.service.jwt;

import com.soopgyeol.api.domain.user.Role;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {


    private final String secret;

    public JwtProvider(Dotenv dotenv) {
        this.secret = dotenv.get("JWT_SECRET");
    }

    /** 액세스 토큰 유효기간*/
    private final long ACCESS_VALIDITY = 1000 * 60 * 60 * 2;   // 2h


    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);   // HS256 자동
    }

    /** 토큰 생성 */
    public String createToken(Long userId, Role role) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role.name())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_VALIDITY))
                .signWith(getSigningKey())   // 키만 넘기면 HS256
                .compact();
    }


    public Claims parse(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)   // 서명·만료 자동 확인
                .getBody();              // -> Claims
    }
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject()); // subject에 userId 저장된 경우
    }
}


