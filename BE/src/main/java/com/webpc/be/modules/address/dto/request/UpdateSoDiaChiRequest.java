package com.webpc.be.modules.address.dto.request;

public record UpdateSoDiaChiRequest(
    String tenNguoiNhan,
    String soDienThoai,
    String diaChiCuThe,
    String tinhThanhId,
    String quanHuyenId,
    String phuongXaId,
    Boolean macDinh
) {
}
