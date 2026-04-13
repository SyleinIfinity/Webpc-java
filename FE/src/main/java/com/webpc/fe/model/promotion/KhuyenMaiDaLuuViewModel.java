package com.webpc.fe.model.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KhuyenMaiDaLuuViewModel {

    private Integer maKMKH;
    private Integer maKhuyenMai;
    private String maCodeKM;
    private String tenChuongTrinh;
    private String loaiGiam;
    private BigDecimal giaTriGiam = BigDecimal.ZERO;
    private BigDecimal donHangToiThieu = BigDecimal.ZERO;
    private BigDecimal giamToiDa;
    private LocalDateTime ngayKetThuc;

    public String getNgayKetThucDisplay() {
        if (ngayKetThuc == null) {
            return "";
        }
        return ngayKetThuc.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
