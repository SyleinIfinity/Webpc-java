package com.webpc.be.modules.inventory.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PhieuNhapResponse(
    Integer maPhieuNhap,
    String maCodePhieu,
    LocalDateTime ngayNhap,
    BigDecimal tongTienNhap,
    String tenNhanVien,
    String ghiChu,
    List<ChiTietPhieuNhapResponse> chiTiet
) {
    public record ChiTietPhieuNhapResponse(
        Integer maChiTietPhieuNhap,
        Integer maSanPham,
        String tenSanPham,
        Integer soLuongNhap,
        BigDecimal giaNhap
    ) {
        public BigDecimal thanhTien() {
            return giaNhap.multiply(BigDecimal.valueOf(soLuongNhap));
        }
    }
}
