package com.webpc.be.modules.catalog.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
    Integer maSanPham,
    String tenSanPham,
    BigDecimal giaBan,
    BigDecimal giaKhuyenMai,
    boolean trangThai,
    Integer soLuongTon,
    String moTa,
    String tenDanhMuc,
    List<ImageResponse> danhSachAnh
) {
}
