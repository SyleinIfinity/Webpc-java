package com.webpc.be.security;

import com.webpc.be.security.principal.WebpcUserPrincipal;
import java.util.Objects;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null
            && authentication.isAuthenticated()
            && authentication.getPrincipal() instanceof WebpcUserPrincipal;
    }

    public WebpcUserPrincipal getPrincipal(Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw new AccessDeniedException("Vui long dang nhap de tiep tuc.");
        }
        return (WebpcUserPrincipal) authentication.getPrincipal();
    }

    public boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "Admin");
    }

    public boolean isSales(Authentication authentication) {
        return hasRole(authentication, "Sales");
    }

    public boolean isKho(Authentication authentication) {
        return hasRole(authentication, "Kho");
    }

    public boolean isCustomer(Authentication authentication) {
        return hasRole(authentication, "KhachHang");
    }

    public boolean canAccessCustomer(Authentication authentication, Integer maKhachHang) {
        if (maKhachHang == null || !isAuthenticated(authentication)) {
            return false;
        }
        if (isAdmin(authentication) || isSales(authentication)) {
            return true;
        }
        if (isCustomer(authentication)) {
            return Objects.equals(getPrincipal(authentication).maKhachHang(), maKhachHang);
        }
        return false;
    }

    public void requireCustomerAccess(Authentication authentication, Integer maKhachHang) {
        if (!canAccessCustomer(authentication, maKhachHang)) {
            throw new AccessDeniedException("Ban khong duoc truy cap du lieu khach hang nay.");
        }
    }

    public boolean canAccessAccount(Authentication authentication, Integer maTaiKhoan) {
        if (maTaiKhoan == null || !isAuthenticated(authentication)) {
            return false;
        }
        if (isAdmin(authentication)) {
            return true;
        }
        return Objects.equals(getPrincipal(authentication).maTaiKhoan(), maTaiKhoan);
    }

    public void requireAccountAccess(Authentication authentication, Integer maTaiKhoan) {
        if (!canAccessAccount(authentication, maTaiKhoan)) {
            throw new AccessDeniedException("Ban khong duoc thao tac tren tai khoan nay.");
        }
    }

    private boolean hasRole(Authentication authentication, String expectedRole) {
        if (!isAuthenticated(authentication)) {
            return false;
        }
        String currentRole = getPrincipal(authentication).vaiTro();
        return currentRole != null && currentRole.equalsIgnoreCase(expectedRole);
    }
}
