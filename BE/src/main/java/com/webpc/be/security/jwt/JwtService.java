package com.webpc.be.security.jwt;

import com.webpc.be.common.config.properties.JwtProperties;
import com.webpc.be.security.principal.WebpcUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generateToken(WebpcUserPrincipal principal) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(principal.tenDangNhap())
            .issuer(jwtProperties.getIssuer())
            .audience().add(jwtProperties.getAudience()).and()
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(jwtProperties.getExpirationHours(), ChronoUnit.HOURS)))
            .claim("MaTaiKhoan", principal.maTaiKhoan())
            .claim("MaNhanVien", principal.maNhanVien())
            .claim("MaKhachHang", principal.maKhachHang())
            .claim("Role", principal.vaiTro())
            .signWith(getSigningKey())
            .compact();
    }

    public Optional<WebpcUserPrincipal> extractPrincipal(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            Integer maTaiKhoan = claims.get("MaTaiKhoan", Integer.class);
            Integer maNhanVien = claims.get("MaNhanVien", Integer.class);
            Integer maKhachHang = claims.get("MaKhachHang", Integer.class);
            String role = claims.get("Role", String.class);
            String tenDangNhap = claims.getSubject();

            return Optional.of(new WebpcUserPrincipal(
                maTaiKhoan,
                maNhanVien,
                maKhachHang,
                tenDangNhap,
                role
            ));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private SecretKey getSigningKey() {
        String key = jwtProperties.getKey();
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("app.jwt.key chua duoc cau hinh.");
        }

        try {
            byte[] decoded = Decoders.BASE64.decode(key);
            if (decoded.length >= 32) {
                return Keys.hmacShaKeyFor(decoded);
            }
        } catch (Exception ignored) {
        }

        byte[] raw = key.getBytes(StandardCharsets.UTF_8);
        if (raw.length < 32) {
            throw new IllegalStateException("app.jwt.key phai co it nhat 32 byte.");
        }
        return Keys.hmacShaKeyFor(raw);
    }
}
