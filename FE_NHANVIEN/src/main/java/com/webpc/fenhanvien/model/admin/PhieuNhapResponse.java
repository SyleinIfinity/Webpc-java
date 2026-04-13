package com.webpc.fenhanvien.model.admin;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PhieuNhapResponse {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Integer maPhieuNhap;
    private String maCodePhieu;
    private LocalDateTime ngayNhap;
    private BigDecimal tongTienNhap;
    private String tenNhanVien;
    private String ghiChu;
    private List<ChiTietPhieuNhapResponse> chiTiet;

    public String getNgayNhapDisplay() {
        return ngayNhap == null ? "" : ngayNhap.format(DATE_TIME_FORMATTER);
    }

    @Data
    public static class ChiTietPhieuNhapResponse {
        private Integer maChiTietPhieuNhap;
        private Integer maSanPham;
        private String tenSanPham;
        private Integer soLuongNhap;
        private BigDecimal giaNhap;
    }
}
