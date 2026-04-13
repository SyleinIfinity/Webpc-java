package com.webpc.be.modules.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webpc.be.common.util.FileStorageService;
import com.webpc.be.modules.user.dto.request.LoginRequest;
import com.webpc.be.modules.user.dto.response.LoginResponse;
import com.webpc.be.modules.user.entity.KhachHang;
import com.webpc.be.modules.user.entity.TaiKhoan;
import com.webpc.be.modules.user.repository.KhachHangRepository;
import com.webpc.be.modules.user.repository.NhanVienRepository;
import com.webpc.be.modules.user.repository.TaiKhoanRepository;
import com.webpc.be.modules.user.repository.VaiTroRepository;
import com.webpc.be.security.jwt.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private NhanVienRepository nhanVienRepository;

    @Mock
    private TaiKhoanRepository taiKhoanRepository;

    @Mock
    private VaiTroRepository vaiTroRepository;

    @Mock
    private KhachHangRepository khachHangRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private FileStorageService fileStorageService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
            nhanVienRepository,
            taiKhoanRepository,
            vaiTroRepository,
            khachHangRepository,
            passwordEncoder,
            jwtService,
            fileStorageService
        );
    }

    @Test
    void loginUpgradesLegacyPlaintextPasswordToBcrypt() {
        TaiKhoan taiKhoan = buildCustomerAccount("legacy-user", "123456");

        when(taiKhoanRepository.findByTenDangNhap("legacy-user")).thenReturn(Optional.of(taiKhoan));
        when(passwordEncoder.encode("123456")).thenReturn("$2a$10$encoded-password");
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        LoginResponse response = userService.login(new LoginRequest("legacy-user", "123456"));

        assertThat(response).isNotNull();
        assertThat(taiKhoan.getMatKhauHash()).isEqualTo("$2a$10$encoded-password");
        verify(taiKhoanRepository).save(taiKhoan);
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void loginWithBcryptPasswordDoesNotRewriteHash() {
        TaiKhoan taiKhoan = buildCustomerAccount("bcrypt-user", "$2a$10$already-bcrypt");

        when(taiKhoanRepository.findByTenDangNhap("bcrypt-user")).thenReturn(Optional.of(taiKhoan));
        when(passwordEncoder.matches("123456", "$2a$10$already-bcrypt")).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        LoginResponse response = userService.login(new LoginRequest("bcrypt-user", "123456"));

        assertThat(response).isNotNull();
        verify(passwordEncoder).matches("123456", "$2a$10$already-bcrypt");
        verify(taiKhoanRepository, never()).save(any(TaiKhoan.class));
        verify(passwordEncoder, never()).encode(eq("123456"));
    }

    private TaiKhoan buildCustomerAccount(String username, String passwordHash) {
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setMaTaiKhoan(1);
        taiKhoan.setTenDangNhap(username);
        taiKhoan.setMatKhauHash(passwordHash);
        taiKhoan.setTrangThai("Active");

        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(1);
        khachHang.setHoTen("Test Customer");
        khachHang.setTaiKhoan(taiKhoan);

        taiKhoan.setKhachHang(khachHang);
        return taiKhoan;
    }
}
