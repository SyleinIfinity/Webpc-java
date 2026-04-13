package com.webpc.be.modules.order.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.exception.ResourceNotFoundException;
import com.webpc.be.modules.catalog.entity.HinhAnhSanPham;
import com.webpc.be.modules.catalog.entity.SanPham;
import com.webpc.be.modules.catalog.repository.HinhAnhSanPhamRepository;
import com.webpc.be.modules.catalog.repository.SanPhamRepository;
import com.webpc.be.modules.order.dto.request.RejectOrderRequest;
import com.webpc.be.modules.order.dto.response.DonHangListResponse;
import com.webpc.be.modules.order.dto.response.DonHangResponse;
import com.webpc.be.modules.order.entity.DonHang;
import com.webpc.be.modules.order.repository.ChiTietDonHangRepository;
import com.webpc.be.modules.order.repository.DonHangRepository;
import com.webpc.be.modules.order.repository.GiaoDichThanhToanRepository;
import com.webpc.be.security.AuthorizationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DonHangService {

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final GiaoDichThanhToanRepository giaoDichThanhToanRepository;
    private final SanPhamRepository sanPhamRepository;
    private final HinhAnhSanPhamRepository hinhAnhSanPhamRepository;
    private final AuthorizationService authorizationService;

    @Transactional(readOnly = true)
    public List<DonHangListResponse> getAll() {
        return donHangRepository.findAllForList();
    }

    @Transactional(readOnly = true)
    public DonHangResponse getById(Integer id) {
        if (id == null) {
            return null;
        }
        DonHang donHang = donHangRepository.findById(id).orElse(null);
        if (donHang == null) {
            return null;
        }

        var chiTietEntities = chiTietDonHangRepository.findByMaDonHangFetchSanPham(id);
        var giaoDichEntities = giaoDichThanhToanRepository.findByMaDonHangOrderByMaGiaoDichAsc(id);

        List<DonHangResponse.ChiTietDonHangResponse> chiTiet = chiTietEntities.stream().map(ct -> {
            String tenSanPham = ct.getSanPham() != null ? ct.getSanPham().getTenSanPham() : "San pham da xoa";
            String hinhAnh = resolveProductImage(ct.getMaSanPham());
            return new DonHangResponse.ChiTietDonHangResponse(
                ct.getMaSanPham(),
                tenSanPham,
                hinhAnh,
                ct.getSoLuong(),
                ct.getDonGiaLucMua(),
                ct.getThanhTien()
            );
        }).toList();

        List<DonHangResponse.GiaoDichResponse> giaoDichs = giaoDichEntities.stream().map(gd -> new DonHangResponse.GiaoDichResponse(
            gd.getMaGiaoDich(),
            gd.getNgayTao(),
            gd.getSoTien(),
            gd.getTrangThai(),
            gd.getPhuongThuc()
        )).toList();

        return new DonHangResponse(
            donHang.getMaDonHang(),
            donHang.getMaCodeDonHang(),
            donHang.getMaKhachHang(),
            donHang.getNgayDat(),
            donHang.getTrangThai(),
            donHang.getPhuongThucThanhToan(),
            donHang.getNguoiNhan(),
            donHang.getSoDienThoaiGiao(),
            donHang.getDiaChiGiaoHang(),
            defaultBigDecimal(donHang.getPhiVanChuyen()),
            defaultBigDecimal(donHang.getTongTien()),
            chiTiet,
            giaoDichs
        );
    }

    @Transactional
    public boolean approve(Integer id, Authentication authentication) {
        DonHang donHang = donHangRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay don hang."));

        if ("Huy".equalsIgnoreCase(donHang.getTrangThai()) || "HoanThanh".equalsIgnoreCase(donHang.getTrangThai())) {
            throw new BadRequestException("Don hang da o trang thai ket thuc, khong the duyet.");
        }

        donHang.setTrangThai("DangGiao");
        setApprover(donHang, authentication);
        donHangRepository.save(donHang);
        return true;
    }

    @Transactional
    public boolean reject(Integer id, RejectOrderRequest request, Authentication authentication) {
        DonHang donHang = donHangRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay don hang."));

        if ("Huy".equalsIgnoreCase(donHang.getTrangThai()) || "HoanThanh".equalsIgnoreCase(donHang.getTrangThai())) {
            throw new BadRequestException("Don hang da o trang thai ket thuc, khong the huy.");
        }

        // Tra kho (best-effort) theo chi tiet don hang
        var chiTietEntities = chiTietDonHangRepository.findByMaDonHangFetchSanPham(id);
        for (var ct : chiTietEntities) {
            Integer maSanPham = ct.getMaSanPham();
            Integer soLuong = ct.getSoLuong();
            if (maSanPham == null || soLuong == null || soLuong <= 0) {
                continue;
            }
            Optional<SanPham> spOpt = sanPhamRepository.findById(maSanPham);
            if (spOpt.isEmpty()) {
                continue;
            }
            SanPham sp = spOpt.get();
            int current = sp.getSoLuongTon() == null ? 0 : sp.getSoLuongTon();
            sp.setSoLuongTon(current + soLuong);
            sanPhamRepository.save(sp);
        }

        donHang.setTrangThai("Huy");
        setApprover(donHang, authentication);
        donHangRepository.save(donHang);
        return true;
    }

    private void setApprover(DonHang donHang, Authentication authentication) {
        try {
            Integer maNhanVien = authorizationService.getPrincipal(authentication).maNhanVien();
            if (maNhanVien != null) {
                donHang.setMaNhanVienDuyet(maNhanVien);
            }
        } catch (Exception ignored) {
        }
    }

    private BigDecimal defaultBigDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String resolveProductImage(Integer maSanPham) {
        if (maSanPham == null) {
            return null;
        }
        List<HinhAnhSanPham> images = hinhAnhSanPhamRepository.findByMaSanPhamOrderByIdAsc(maSanPham);
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
            .filter(HinhAnhSanPham::isLaAnhDaiDien)
            .findFirst()
            .orElse(images.get(0))
            .getUrlHinhAnh();
    }
}
