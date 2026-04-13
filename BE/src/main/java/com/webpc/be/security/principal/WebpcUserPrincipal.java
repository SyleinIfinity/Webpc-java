package com.webpc.be.security.principal;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public record WebpcUserPrincipal(
    Integer maTaiKhoan,
    Integer maNhanVien,
    Integer maKhachHang,
    String tenDangNhap,
    String vaiTro
) {

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (vaiTro == null || vaiTro.isBlank()) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + vaiTro));
    }
}
