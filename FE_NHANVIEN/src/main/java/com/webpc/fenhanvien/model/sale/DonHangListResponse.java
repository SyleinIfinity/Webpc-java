package com.webpc.fenhanvien.model.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonHangListResponse {
    private Integer maDonHang;
    private String maCodeDonHang;
    private Integer maKhachHang;
    private String tenKhachHang;
    private LocalDateTime ngayDat;
    private BigDecimal tongTien = BigDecimal.ZERO;
    private String trangThai;
    private String phuongThucThanhToan;

    public String getNgayDatDisplay() {
        if (ngayDat == null) {
            return "";
        }
        return ngayDat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}

