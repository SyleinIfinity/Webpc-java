package com.webpc.be.modules.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSoDiaChiRequest(
    @NotNull Integer maKhachHang,
    @NotBlank(message = "Ten nguoi nhan la bat buoc") String tenNguoiNhan,
    @NotBlank(message = "So dien thoai la bat buoc") String soDienThoai,
    @NotBlank String diaChiCuThe,
    @NotBlank String tinhThanhId,
    @NotBlank String quanHuyenId,
    @NotBlank String phuongXaId,
    boolean macDinh
) {
}
