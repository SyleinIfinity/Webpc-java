package com.webpc.be.modules.address.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SoDiaChiResponse(
    Integer maSoDiaChi,
    Integer maKhachHang,
    String tenNguoiNhan,
    String soDienThoai,
    String diaChiCuThe,
    String tinhThanhId,
    String tenTinhThanh,
    String quanHuyenId,
    String tenQuanHuyen,
    String phuongXaId,
    String tenPhuongXa,
    boolean macDinh
) {

    @JsonProperty("diaChiDayDu")
    public String diaChiDayDu() {
        return "%s, %s, %s, %s".formatted(diaChiCuThe, tenPhuongXa, tenQuanHuyen, tenTinhThanh);
    }
}
