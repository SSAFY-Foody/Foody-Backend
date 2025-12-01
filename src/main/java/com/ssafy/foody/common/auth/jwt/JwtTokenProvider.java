package com.ssafy.foody.common.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

    // 비밀키 (실무에선 application.yml에서 @Value로 관리해야 함)
    // 32바이트 이상이어야 함
	// 지금은 그냥 하드코딩
    private final Key key = Keys.hmacShaKeyFor("thisIsMySecretKeyForFoodyProject1234".getBytes());
    
    private final long VALID_TIME = 30 * 60 * 1000L; // 30분

    // 토큰 생성
    public String createToken(String userPk) {
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject(userPk) // id
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + VALID_TIME))
                // HS256 (대칭키) - 같은 서버에서 생성과 검사를 하므로
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        log.debug("#############################################");
        log.debug("발급된 JWT 토큰: Bearer {}", token);
        log.debug("#############################################");
        
        return token;
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // 헤더에서 토큰 꺼내기 ("Bearer " 제거)
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
