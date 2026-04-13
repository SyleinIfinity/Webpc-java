package com.webpc.fe.model.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KhuyenMaiKhachHangResponse {

    private Integer maKMKH;
    private Integer maKhuyenMai;
    private Integer maKhachHang;
    private String maCodeKM;
    private String tenChuongTrinh;
    private boolean daSuDung;
    private LocalDateTime ngayThuThap;
    private BigDecimal giaTriGiam = BigDecimal.ZERO;
    private String loaiGiam;
    private BigDecimal donHangToiThieu = BigDecimal.ZERO;
    private BigDecimal giamToiDa;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
}
