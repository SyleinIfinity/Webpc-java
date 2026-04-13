package com.webpc.be.modules.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonHangListResponse(
    Integer maDonHang,
    String maCodeDonHang,
    Integer maKhachHang,
    String tenKhachHang,
    LocalDateTime ngayDat,
    BigDecimal tongTien,
    String trangThai,
    String phuongThucThanhToan
) {
}

