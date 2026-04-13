package com.webpc.fe.model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LichSuDonHangViewModel {

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
    private BigDecimal giamGia = BigDecimal.ZERO;
    private List<ChiTietDonHangViewModel> chiTiet = new ArrayList<>();
    private List<GiaoDichViewModel> giaoDichs = new ArrayList<>();

    public String getNgayDatDisplay() {
        if (ngayDat == null) {
            return "";
        }
        return ngayDat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
