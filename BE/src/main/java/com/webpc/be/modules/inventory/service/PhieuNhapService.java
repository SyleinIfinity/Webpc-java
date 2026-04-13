package com.webpc.be.modules.inventory.service;

import com.webpc.be.common.exception.BadRequestException;
import com.webpc.be.common.exception.ResourceNotFoundException;
import com.webpc.be.modules.catalog.entity.SanPham;
import com.webpc.be.modules.catalog.repository.SanPhamRepository;
import com.webpc.be.modules.inventory.dto.request.CreatePhieuNhapRequest;
import com.webpc.be.modules.inventory.dto.request.UpdatePhieuNhapRequest;
import com.webpc.be.modules.inventory.dto.response.PhieuNhapResponse;
import com.webpc.be.modules.inventory.entity.ChiTietPhieuNhap;
import com.webpc.be.modules.inventory.entity.PhieuNhap;
import com.webpc.be.modules.inventory.repository.ChiTietPhieuNhapRepository;
import com.webpc.be.modules.inventory.repository.PhieuNhapRepository;
import com.webpc.be.modules.user.repository.NhanVienRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final SanPhamRepository sanPhamRepository;
    private final NhanVienRepository nhanVienRepository;

    @Transactional(readOnly = true)
    public List<PhieuNhapResponse> getAll() {
        return phieuNhapRepository.findAllByOrderByMaPhieuNhapAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public PhieuNhapResponse getById(Integer id) {
        return phieuNhapRepository.findByMaPhieuNhap(id)
            .map(this::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phieu nhap"));
    }

    @Transactional
    public PhieuNhapResponse create(CreatePhieuNhapRequest request) {
        if (nhanVienRepository.findByMaNhanVien(request.getMaNhanVienNhap()).isEmpty()) {
            throw new BadRequestException("Nhan vien nhap khong ton tai.");
        }

        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setMaCodePhieu(generateCode());
        phieuNhap.setMaNhanVienNhap(request.getMaNhanVienNhap());
        phieuNhap.setGhiChu(request.getGhiChu());
        phieuNhap.setNgayNhap(LocalDateTime.now());

        List<ChiTietPhieuNhap> details = new ArrayList<>();
        BigDecimal tongTien = BigDecimal.ZERO;
        for (CreatePhieuNhapRequest.ChiTietPhieuNhapItem item : request.getChiTiet()) {
            SanPham product = sanPhamRepository.findByMaSanPham(item.getMaSanPham())
                .orElseThrow(() -> new BadRequestException("San pham " + item.getMaSanPham() + " khong ton tai."));

            ChiTietPhieuNhap detail = new ChiTietPhieuNhap();
            detail.setMaSanPham(item.getMaSanPham());
            detail.setSoLuongNhap(item.getSoLuongNhap());
            detail.setGiaNhap(item.getGiaNhap());
            details.add(detail);

            tongTien = tongTien.add(item.getGiaNhap().multiply(BigDecimal.valueOf(item.getSoLuongNhap())));

            product.setSoLuongTon(product.getSoLuongTon() + item.getSoLuongNhap());
            product.setGiaBan(item.getGiaNhap().multiply(BigDecimal.valueOf(1.2)).setScale(2, RoundingMode.HALF_UP));
            sanPhamRepository.save(product);
        }

        phieuNhap.setTongTienNhap(tongTien);
        phieuNhapRepository.save(phieuNhap);

        for (ChiTietPhieuNhap detail : details) {
            detail.setMaPhieuNhap(phieuNhap.getMaPhieuNhap());
        }
        chiTietPhieuNhapRepository.saveAll(details);

        return getById(phieuNhap.getMaPhieuNhap());
    }

    @Transactional
    public PhieuNhapResponse update(Integer id, UpdatePhieuNhapRequest request) {
        PhieuNhap phieuNhap = phieuNhapRepository.findByMaPhieuNhap(id)
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay phieu nhap de cap nhat"));
        if (request.ghiChu() != null) {
            phieuNhap.setGhiChu(request.ghiChu());
        }
        phieuNhapRepository.save(phieuNhap);
        return toResponse(phieuNhap);
    }

    @Transactional
    public boolean delete(Integer id) {
        PhieuNhap phieuNhap = phieuNhapRepository.findByMaPhieuNhap(id).orElse(null);
        if (phieuNhap == null) {
            return false;
        }

        for (ChiTietPhieuNhap detail : phieuNhap.getChiTietPhieuNhaps()) {
            SanPham product = sanPhamRepository.findByMaSanPham(detail.getMaSanPham())
                .orElseThrow(() -> new BadRequestException("San pham " + detail.getMaSanPham() + " khong ton tai."));
            int soLuongMoi = product.getSoLuongTon() - detail.getSoLuongNhap();
            if (soLuongMoi < 0) {
                throw new BadRequestException("Khong the xoa phieu nhap vi ton kho se am cho san pham " + product.getMaSanPham());
            }
            product.setSoLuongTon(soLuongMoi);
            sanPhamRepository.save(product);
        }

        phieuNhapRepository.delete(phieuNhap);
        return true;
    }

    private PhieuNhapResponse toResponse(PhieuNhap entity) {
        return new PhieuNhapResponse(
            entity.getMaPhieuNhap(),
            entity.getMaCodePhieu(),
            entity.getNgayNhap(),
            entity.getTongTienNhap(),
            entity.getNhanVien() != null ? entity.getNhanVien().getHoTen() : "N/A",
            entity.getGhiChu(),
            entity.getChiTietPhieuNhaps().stream()
                .map(item -> new PhieuNhapResponse.ChiTietPhieuNhapResponse(
                    item.getMaChiTietPhieuNhap(),
                    item.getMaSanPham(),
                    item.getSanPham() != null ? item.getSanPham().getTenSanPham() : "Unknown",
                    item.getSoLuongNhap(),
                    item.getGiaNhap()
                ))
                .toList()
        );
    }

    private String generateCode() {
        return "PN" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"))
            + ThreadLocalRandom.current().nextInt(1000, 10000);
    }
}
