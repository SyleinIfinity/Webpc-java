package com.webpc.fenhanvien.model.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonHangResponse {
    private Integer maDonHang;
    private String maCodeDonHang;
    private Integer maKhachHang;
    private LocalDateTime ngayDat;
    private String trangThai;
    private String phuongThucThanhToan;
    private String nguoiNhan;
    private String soDienThoaiGiao;
    private String diaChiGiaoHang;
    private BigDecimal phiVanChuyen = BigDecimal.ZERO;
    private BigDecimal tongTien = BigDecimal.ZERO;
    private List<ChiTietDonHangResponse> chiTiet = new ArrayList<>();
    private List<GiaoDichResponse> giaoDichs = new ArrayList<>();

    public String getNgayDatDisplay() {
        if (ngayDat == null) {
            return "";
        }
        return ngayDat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Data
    @NoArgsConstructor
    public static class ChiTietDonHangResponse {
        private Integer maSanPham;
        private String tenSanPham;
        private String hinhAnh;
        private Integer soLuong;
        private BigDecimal donGiaLucMua = BigDecimal.ZERO;
        private BigDecimal thanhTien = BigDecimal.ZERO;
    }

    @Data
    @NoArgsConstructor
    public static class GiaoDichResponse {
        private Integer maGiaoDich;
        private LocalDateTime ngayGiaoDich;
        private BigDecimal soTien = BigDecimal.ZERO;
        private String trangThai;
        private String phuongThuc;

        public String getNgayGiaoDichDisplay() {
            if (ngayGiaoDich == null) {
                return "";
            }
            return ngayGiaoDich.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
    }
}

