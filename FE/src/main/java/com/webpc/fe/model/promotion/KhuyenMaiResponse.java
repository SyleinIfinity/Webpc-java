package com.webpc.fe.model.promotion;

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
    private BigDecimal giaTriGiam = BigDecimal.ZERO;
    private String loaiGiam;
    private BigDecimal donHangToiThieu = BigDecimal.ZERO;
    private BigDecimal giamToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private int soLuongConLai;
    private boolean coTheLuu;

    public String getNgayBatDauDisplay() {
        if (ngayBatDau == null) {
            return "";
        }
        return ngayBatDau.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getNgayKetThucDisplay() {
        if (ngayKetThuc == null) {
            return "";
        }
        return ngayKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
