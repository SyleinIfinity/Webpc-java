package com.webpc.be.modules.user.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.exception.ResourceNotFoundException;
import com.webpc.be.common.util.FileStorageService;
import com.webpc.be.modules.user.dto.request.KhachHangRequest;
import com.webpc.be.modules.user.dto.request.LoginRequest;
import com.webpc.be.modules.user.dto.request.NhanVienRequest;
import com.webpc.be.modules.user.dto.request.UpdateKhachHangRequest;
import com.webpc.be.modules.user.dto.request.VaiTroRequest;
import com.webpc.be.modules.user.dto.response.KhachHangResponse;
import com.webpc.be.modules.user.dto.response.LoginResponse;
import com.webpc.be.modules.user.dto.response.NhanVienResponse;
import com.webpc.be.modules.user.dto.response.TaiKhoanResponse;
import com.webpc.be.modules.user.dto.response.VaiTroResponse;
import com.webpc.be.modules.user.entity.KhachHang;
import com.webpc.be.modules.user.entity.NhanVien;
import com.webpc.be.modules.user.entity.TaiKhoan;
import com.webpc.be.modules.user.entity.VaiTro;
import com.webpc.be.modules.user.repository.KhachHangRepository;
import com.webpc.be.modules.user.repository.NhanVienRepository;
import com.webpc.be.modules.user.repository.TaiKhoanRepository;
import com.webpc.be.modules.user.repository.VaiTroRepository;
import com.webpc.be.security.jwt.JwtService;
import com.webpc.be.security.principal.WebpcUserPrincipal;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<VaiTroResponse> getAllVaiTros() {
        return vaiTroRepository.findAll().stream()
            .map(this::toVaiTroResponse)
            .toList();
    }

    @Transactional
    public void createVaiTro(VaiTroRequest request) {
        if (request.tenVaiTro() == null || request.tenVaiTro().isBlank()) {
            throw new BadRequestException("Ten vai tro khong duoc de trong.");
        }
        if (vaiTroRepository.findByTenVaiTro(request.tenVaiTro()).isPresent()) {
            throw new BadRequestException("Vai tro da ton tai.");
        }

        VaiTro vaiTro = new VaiTro();
        vaiTro.setTenVaiTro(request.tenVaiTro());
        vaiTro.setMoTa(request.moTa());
        vaiTroRepository.save(vaiTro);
    }

    @Transactional
    public boolean deleteVaiTro(Integer id) {
        if (!vaiTroRepository.existsById(id)) {
            return false;
        }
        vaiTroRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<NhanVienResponse> getAllNhanViens() {
        return nhanVienRepository.findAllByOrderByMaNhanVienAsc().stream()
            .map(this::toNhanVienResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public NhanVienResponse getNhanVienById(Integer id) {
        return nhanVienRepository.findByMaNhanVien(id)
            .map(this::toNhanVienResponse)
            .orElse(null);
    }

    @Transactional
    public String createNhanVien(NhanVienRequest request) {
        if (request.tenDangNhap() == null || request.tenDangNhap().isBlank()
            || request.matKhau() == null || request.matKhau().isBlank()) {
            throw new BadRequestException("Ten dang nhap va mat khau khong duoc de trong.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new BadRequestException("Email nhan vien khong duoc de trong.");
        }
        if (taiKhoanRepository.existsByTenDangNhap(request.tenDangNhap())) {
            throw new BadRequestException("Ten dang nhap '" + request.tenDangNhap() + "' da ton tai.");
        }
        if (taiKhoanRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email '" + request.email() + "' da duoc su dung.");
        }

        VaiTro vaiTro = vaiTroRepository.findById(request.maVaiTro())
            .orElseThrow(() -> new BadRequestException("Vai tro khong ton tai."));

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(request.tenDangNhap());
        taiKhoan.setEmail(request.email());
        taiKhoan.setMatKhauHash(passwordEncoder.encode(request.matKhau()));
        taiKhoan.setTrangThai("Active");
        taiKhoan.setNgayTao(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);

        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaCodeNhanVien(generateNhanVienCode());
        nhanVien.setHoTen(request.hoTen());
        nhanVien.setSoDienThoai(request.soDienThoai());
        nhanVien.setVaiTro(vaiTro);
        nhanVien.setTaiKhoan(taiKhoan);
        nhanVienRepository.save(nhanVien);
        return "Tao nhan vien thanh cong.";
    }

    @Transactional
    public boolean updateNhanVien(Integer id, NhanVienRequest request) {
        NhanVien nhanVien = nhanVienRepository.findByMaNhanVien(id).orElse(null);
        if (nhanVien == null) {
            return false;
        }

        VaiTro vaiTro = vaiTroRepository.findById(request.maVaiTro())
            .orElseThrow(() -> new BadRequestException("Vai tro khong ton tai."));

        nhanVien.setHoTen(request.hoTen());
        nhanVien.setSoDienThoai(request.soDienThoai());
        nhanVien.setVaiTro(vaiTro);
        nhanVienRepository.save(nhanVien);
        return true;
    }

    @Transactional
    public boolean deleteNhanVien(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findByMaNhanVien(id).orElse(null);
        if (nhanVien == null) {
            return false;
        }

        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        nhanVienRepository.delete(nhanVien);
        if (taiKhoan != null) {
            taiKhoanRepository.delete(taiKhoan);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<KhachHangResponse> getAllKhachHangs() {
        return khachHangRepository.findAllByOrderByMaKhachHangAsc().stream()
            .map(this::toKhachHangResponse)
            .toList();
    }

    @Transactional
    public String createKhachHang(KhachHangRequest request) {
        TaiKhoan taiKhoan = null;
        boolean hasAccountPayload = request.tenDangNhap() != null && !request.tenDangNhap().isBlank()
            && request.matKhau() != null && !request.matKhau().isBlank();

        if (hasAccountPayload) {
            if (request.email() == null || request.email().isBlank()) {
                throw new BadRequestException("Email la bat buoc khi tao tai khoan cho khach hang.");
            }
            if (taiKhoanRepository.existsByTenDangNhap(request.tenDangNhap())) {
                throw new BadRequestException("Ten dang nhap da ton tai.");
            }
            if (taiKhoanRepository.existsByEmail(request.email())) {
                throw new BadRequestException("Email da duoc su dung cho tai khoan khac.");
            }

            taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap(request.tenDangNhap());
            taiKhoan.setEmail(request.email());
            taiKhoan.setMatKhauHash(passwordEncoder.encode(request.matKhau()));
            taiKhoan.setTrangThai("Active");
            taiKhoan.setNgayTao(LocalDateTime.now());
            taiKhoanRepository.save(taiKhoan);
        }

        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(request.hoTen());
        khachHang.setSoDienThoai(request.soDienThoai());
        khachHang.setEmail(request.email());
        khachHang.setTaiKhoan(taiKhoan);
        khachHangRepository.save(khachHang);
        return "Them khach hang thanh cong.";
    }

    @Transactional
    public boolean updateKhachHang(Integer id, UpdateKhachHangRequest request) {
        KhachHang khachHang = khachHangRepository.findByMaKhachHang(id).orElse(null);
        if (khachHang == null) {
            return false;
        }
        khachHang.setHoTen(request.hoTen());
        khachHang.setSoDienThoai(request.soDienThoai());
        khachHang.setEmail(request.email());
        khachHangRepository.save(khachHang);
        return true;
    }

    @Transactional(readOnly = true)
    public KhachHangResponse getCustomerById(Integer id) {
        KhachHang khachHang = khachHangRepository.findByMaKhachHang(id)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay khach hang voi ma: " + id));
        return toKhachHangResponse(khachHang);
    }

    @Transactional(readOnly = true)
    public List<TaiKhoanResponse> getAllTaiKhoans() {
        return taiKhoanRepository.findAllByOrderByMaTaiKhoanAsc().stream()
            .map(this::toTaiKhoanResponse)
            .toList();
    }

    @Transactional
    public boolean changePassword(Integer id, String newPassword) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        if (taiKhoan == null) {
            return false;
        }
        taiKhoan.setMatKhauHash(passwordEncoder.encode(newPassword));
        taiKhoanRepository.save(taiKhoan);
        return true;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(request.tenDangNhap()).orElse(null);
        if (taiKhoan == null || !authenticatePassword(taiKhoan, request.matKhau())) {
            return null;
        }
        if (!"Active".equalsIgnoreCase(taiKhoan.getTrangThai())) {
            return null;
        }

        Integer maNhanVien = null;
        Integer maKhachHang = null;
        String hoTen = "Unknown";
        int maVaiTro = 0;
        String tenVaiTro = "Unknown";

        if (taiKhoan.getNhanVien() != null) {
            NhanVien nhanVien = taiKhoan.getNhanVien();
            maNhanVien = nhanVien.getMaNhanVien();
            hoTen = nhanVien.getHoTen();
            maVaiTro = nhanVien.getVaiTro() != null ? nhanVien.getVaiTro().getMaVaiTro() : 0;
            tenVaiTro = nhanVien.getVaiTro() != null ? nhanVien.getVaiTro().getTenVaiTro() : "NhanVien";
        } else if (taiKhoan.getKhachHang() != null) {
            KhachHang khachHang = taiKhoan.getKhachHang();
            maKhachHang = khachHang.getMaKhachHang();
            hoTen = khachHang.getHoTen();
            maVaiTro = -1;
            tenVaiTro = "KhachHang";
        }

        String token = jwtService.generateToken(new WebpcUserPrincipal(
            taiKhoan.getMaTaiKhoan(),
            maNhanVien,
            maKhachHang,
            taiKhoan.getTenDangNhap(),
            tenVaiTro
        ));

        return new LoginResponse(
            maNhanVien,
            maKhachHang,
            hoTen,
            taiKhoan.getTenDangNhap(),
            maVaiTro,
            tenVaiTro,
            token
        );
    }

    @Transactional
    public String updateAvatar(Integer id, MultipartFile file) throws IOException {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tai khoan khong ton tai."));

        String oldAvatar = taiKhoan.getAnhDaiDien();
        String newAvatar = fileStorageService.storeAvatar(file);
        taiKhoan.setAnhDaiDien(newAvatar);
        taiKhoanRepository.save(taiKhoan);

        try {
            fileStorageService.deleteByUrl(oldAvatar);
        } catch (IOException ignored) {
        }

        return newAvatar;
    }

    @Transactional
    public void updateStatus(Integer id, String trangThaiMoi) {
        if (!"Active".equals(trangThaiMoi) && !"Locked".equals(trangThaiMoi)) {
            throw new BadRequestException("Trang thai khong hop le (Chi chap nhan 'Active' hoac 'Locked').");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tai khoan khong ton tai."));
        taiKhoan.setTrangThai(trangThaiMoi);
        taiKhoanRepository.save(taiKhoan);
    }

    private VaiTroResponse toVaiTroResponse(VaiTro entity) {
        return new VaiTroResponse(entity.getMaVaiTro(), entity.getTenVaiTro(), entity.getMoTa());
    }

    private NhanVienResponse toNhanVienResponse(NhanVien entity) {
        TaiKhoan taiKhoan = entity.getTaiKhoan();
        VaiTro vaiTro = entity.getVaiTro();
        return new NhanVienResponse(
            entity.getMaNhanVien(),
            entity.getMaCodeNhanVien(),
            entity.getHoTen(),
            entity.getSoDienThoai(),
            vaiTro != null ? vaiTro.getMaVaiTro() : null,
            vaiTro != null ? vaiTro.getTenVaiTro() : "N/A",
            taiKhoan != null ? taiKhoan.getMaTaiKhoan() : null,
            taiKhoan != null ? taiKhoan.getTenDangNhap() : "N/A",
            taiKhoan != null ? taiKhoan.getEmail() : "",
            taiKhoan != null ? taiKhoan.getTrangThai() : "Unknown"
        );
    }

    private KhachHangResponse toKhachHangResponse(KhachHang entity) {
        TaiKhoan taiKhoan = entity.getTaiKhoan();
        return new KhachHangResponse(
            entity.getMaKhachHang(),
            entity.getHoTen(),
            entity.getSoDienThoai(),
            entity.getEmail(),
            taiKhoan != null ? taiKhoan.getMaTaiKhoan() : null,
            taiKhoan != null ? taiKhoan.getTenDangNhap() : null,
            taiKhoan != null
        );
    }

    private TaiKhoanResponse toTaiKhoanResponse(TaiKhoan entity) {
        return new TaiKhoanResponse(
            entity.getMaTaiKhoan(),
            entity.getTenDangNhap(),
            entity.getEmail(),
            entity.getTrangThai(),
            entity.getNgayTao()
        );
    }

    private boolean authenticatePassword(TaiKhoan taiKhoan, String rawPassword) {
        String storedPassword = taiKhoan.getMatKhauHash();
        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }
        try {
            if (isBcryptHash(storedPassword)) {
                return passwordEncoder.matches(rawPassword, storedPassword);
            }
        } catch (Exception ignored) {
            return false;
        }

        if (!storedPassword.equals(rawPassword)) {
            return false;
        }

        // Legacy plaintext passwords are upgraded to bcrypt as soon as the user logs in successfully.
        taiKhoan.setMatKhauHash(passwordEncoder.encode(rawPassword));
        taiKhoanRepository.save(taiKhoan);
        return true;
    }

    private boolean isBcryptHash(String passwordHash) {
        return passwordHash.startsWith("$2a$")
            || passwordHash.startsWith("$2b$")
            || passwordHash.startsWith("$2y$");
    }

    private String generateNhanVienCode() {
        String code;
        do {
            code = "NV" + ThreadLocalRandom.current().nextInt(10000, 100000);
        } while (nhanVienRepository.findByMaCodeNhanVien(code).isPresent());
        return code;
    }
}
