package com.webpc.fenhanvien.model.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KhuyenMaiResponse {
    private Integer maKhuyenMai;
    private String maCodeKM;
    private String tenChuongTrinh;
    private BigDecimal giaTriGiam;
    private String loaiGiam;
    private BigDecimal donHangToiThieu;
    private BigDecimal giamToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private Integer soLuongConLai;

    public String getNgayBatDauDisplay() {
        return formatDate(ngayBatDau);
    }

    public String getNgayKetThucDisplay() {
        return formatDate(ngayKetThuc);
    }

    public String getNgayBatDauIso() {
        return formatIsoDate(ngayBatDau);
    }

    public String getNgayKetThucIso() {
        return formatIsoDate(ngayKetThuc);
    }

    private String formatDate(LocalDateTime value) {
        if (value == null) {
            return "";
        }
        return value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatIsoDate(LocalDateTime value) {
        if (value == null) {
            return "";
        }
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

