package com.webpc.be.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webpc.be.common.response.MessageResponse;
import com.webpc.be.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, ex) ->
                    writeJsonMessage(response, 401, "Vui long dang nhap de tiep tuc."))
                .accessDeniedHandler((request, response, ex) ->
                    writeJsonMessage(response, 403, "Ban khong co quyen truy cap."))
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/health",
                    "/uploads/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST,
                    "/api/TaiKhoan/login",
                    "/api/KhachHang",
                    "/api/Otp/send-otp",
                    "/api/Otp/verify-otp"
                ).permitAll()
                .requestMatchers(HttpMethod.GET,
                    "/api/DanhMuc",
                    "/api/DanhMuc/*",
                    "/api/SanPham",
                    "/api/SanPham/*",
                    "/api/SanPham/danhmuc/*",
                    "/api/HinhAnhSanPham/product/*",
                    "/api/ThongSoKyThuat/sanpham/*",
                    "/api/SoDiaChi/provinces",
                    "/api/SoDiaChi/districts/*",
                    "/api/SoDiaChi/wards/*"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/TaiKhoan").hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.PATCH, "/api/TaiKhoan/update-status/*").hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.GET, "/api/NhanVien", "/api/NhanVien/*").hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.POST, "/api/NhanVien").hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.PUT, "/api/NhanVien/*").hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.DELETE, "/api/NhanVien/*").hasAuthority("ROLE_Admin")
                .requestMatchers("/api/VaiTro/**").hasAuthority("ROLE_Admin")
                .requestMatchers(HttpMethod.GET, "/api/KhachHang").hasAnyAuthority("ROLE_Admin", "ROLE_Sales", "ROLE_NhanVien")
                .requestMatchers("/api/KhuyenMai/**").hasAnyAuthority("ROLE_Admin", "ROLE_Sales", "ROLE_NhanVien")
                .requestMatchers(HttpMethod.GET, "/api/DonHang", "/api/DonHang/*").hasAnyAuthority("ROLE_Admin", "ROLE_Sales", "ROLE_NhanVien")
                .requestMatchers(HttpMethod.PUT, "/api/DonHang/approve/*", "/api/DonHang/reject/*").hasAnyAuthority("ROLE_Admin", "ROLE_Sales", "ROLE_NhanVien")
                .requestMatchers("/api/TaiKhoan/*/change-password", "/api/TaiKhoan/update-avatar/*").authenticated()
                .requestMatchers("/api/KhachHang/*", "/api/SoDiaChi/**").authenticated()
                .requestMatchers("/api/PhieuNhap/**").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .requestMatchers(HttpMethod.POST, "/api/DanhMuc", "/api/SanPham", "/api/ThongSoKyThuat").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .requestMatchers(HttpMethod.PATCH, "/api/DanhMuc/*", "/api/SanPham/*", "/api/ThongSoKyThuat/*").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .requestMatchers(HttpMethod.DELETE, "/api/DanhMuc/*", "/api/SanPham/*", "/api/ThongSoKyThuat/*").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .requestMatchers(HttpMethod.POST, "/api/HinhAnhSanPham/product/*").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .requestMatchers(HttpMethod.PATCH, "/api/HinhAnhSanPham/product/*/set-main/*").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .requestMatchers(HttpMethod.DELETE, "/api/HinhAnhSanPham/*", "/api/HinhAnhSanPham/delete-all/*").hasAnyAuthority("ROLE_Admin", "ROLE_Kho")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(false);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private void writeJsonMessage(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getWriter(), new MessageResponse(message));
    }
}
